package com.haot.reservation.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.reservation.application.dto.ReservationData;
import com.haot.reservation.application.dto.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dto.req.ReservationCancelRequest;
import com.haot.reservation.application.dto.req.ReservationCreateRequest;
import com.haot.reservation.application.dto.req.ReservationSearchRequest;
import com.haot.reservation.application.dto.req.ReservationUpdateRequest;
import com.haot.reservation.application.dto.res.ReservationGetResponse;
import com.haot.reservation.common.exceptions.DateUnavailableException;
import com.haot.reservation.common.exceptions.FeignExceptionUtils;
import com.haot.reservation.common.exceptions.ReservationException;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.common.response.SliceResponse;
import com.haot.reservation.common.response.enums.ErrorCode;
import com.haot.reservation.domain.model.Reservation;
import com.haot.reservation.domain.model.ReservationDate;
import com.haot.reservation.domain.repository.ReservationRepository;
import com.haot.reservation.infrastructure.client.CouponClient;
import com.haot.reservation.infrastructure.client.LodgeClient;
import com.haot.reservation.infrastructure.client.PaymentClient;
import com.haot.reservation.infrastructure.client.PointClient;
import com.haot.reservation.infrastructure.dtos.CouponDataResponse;
import com.haot.reservation.infrastructure.dtos.LodgeDataGetResponse;
import com.haot.reservation.infrastructure.dtos.coupon.FeignConfirmReservationRequest;
import com.haot.reservation.infrastructure.dtos.coupon.FeignVerifyRequest;
import com.haot.reservation.infrastructure.dtos.coupon.ReservationVerifyResponse;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeDateReadResponse;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeDateUpdateStatusRequest;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeReadOneResponse;
import com.haot.reservation.infrastructure.dtos.payment.PaymentCancelRequest;
import com.haot.reservation.infrastructure.dtos.payment.PaymentCreateRequest;
import com.haot.reservation.infrastructure.dtos.payment.PaymentResponse;
import com.haot.reservation.infrastructure.dtos.point.PaymentDataResponse;
import com.haot.reservation.infrastructure.dtos.point.PointStatusRequest;
import com.haot.reservation.infrastructure.dtos.point.PointTransactionRequest;
import com.haot.reservation.infrastructure.enums.ReservationStatus;
import com.haot.submodule.role.Role;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

  private final LodgeClient lodgeClient;
  private final CouponClient couponClient;
  private final PointClient pointClient;
  private final PaymentClient paymentClient;
  private final ReservationRepository reservationRepository;

  @Override
  @Transactional
  public ReservationGetResponse createReservation(
      ReservationCreateRequest request,
      String userId,
      Role role,
      String token
  ) {

    String lodgeName = getLodgeName(request.lodgeId());

    List<LodgeDateReadResponse> availableDates = getAvailableDates(
        request.lodgeId(), request.checkInDate(), request.checkOutDate()
    );

    LodgeDataGetResponse lodgeDataGetResponse = applyLodge(request, availableDates);

    List<String> lodgeDateIds = lodgeDataGetResponse.lodgeDateIds();

    ReservationData reservationData = ReservationData.createWithLodgeData(
        lodgeDateIds, lodgeDataGetResponse.totalPrice()
    );

    ReservationData updateReservationData = applyCouponAndPoint(
        request, userId, role, reservationData
    );

    Reservation reservation = Reservation.createReservation(
        userId,
        lodgeName,
        request.checkInDate(),
        request.checkOutDate(),
        request.numGuests(),
        request.request(),
        updateReservationData.getTotalPrice(),
        updateReservationData.getPaymentId(),
        updateReservationData.getReservationCouponId(),
        updateReservationData.getPointHistoryId()
    );

    List<ReservationDate> reservationDateList = lodgeDateIds.stream()
        .map(dateId -> ReservationDate.create(reservation, dateId)).toList();

    reservation.getDates().addAll(reservationDateList);
    reservationRepository.save(reservation);

    PaymentDataResponse paymentData = requestPayment(
        reservation, userId, role, token, reservationData
    );

    reservation.updatePaymentId(paymentData.paymentId());

    return ReservationGetResponse.of(reservation, paymentData.paymentUrl());
  }

  @Override
  @Transactional(readOnly = true)
  public ReservationGetResponse getReservation(
      String reservationId,
      String userId,
      Role role
  ) {

    Reservation reservation = findReservationById(reservationId);

    validateReservationOwnership(reservation, userId);

    return ReservationGetResponse.of(reservation, null);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReservationGetResponse> searchReservation(
      ReservationSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  ) {
    Page<Reservation> reservations = reservationRepository.searchReservation(request, userId, role,
        pageable);
    return reservations.map(reservation -> ReservationGetResponse.of(reservation, null));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReservationGetResponse> search(
      ReservationAdminSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  ) {
    Page<Reservation> reservations = reservationRepository.search(request, userId, role, pageable);
    return reservations.map(reservation -> ReservationGetResponse.of(reservation, null));
  }

  @Override
  @Transactional
  public void updateReservation(
      ReservationUpdateRequest reservationUpdateRequest,
      String reservationId,
      String userId,
      Role role
  ) {

    Reservation reservation = findReservationById(reservationId);

    switch (reservationUpdateRequest.status()) {
      case "COMPLETED" -> {
        completeReservation(reservation, userId, role);
        reservation.completedReservation();
      }
      case "CANCELED" -> {
        cancelReservation(reservation, userId, role);
        reservation.cancelReservation();
      }
      default -> throw new ReservationException(ErrorCode.RESERVATION_CHANGE_FAILED);
    }
  }

  @Transactional
  public void cancelReservation(
      String reservationId,
      ReservationCancelRequest request,
      String userId,
      Role role
  ) {

    Reservation reservation = findReservationById(reservationId);

    validateReservationOwnership(reservation, userId);
    validateCancellationReason(request.reason());

    String paymentStatus = requestCancelPayment(
        request.reason(), reservation.getPaymentId(), userId, role
    );

    if ("CANCELLED".equals(paymentStatus)) {
      cancelReservation(reservation, userId, role);
      reservation.cancelReservation();
    } else {
      throw new ReservationException(ErrorCode.PAYMENT_ERROR);
    }
  }

  private String getLodgeName(String lodgeId) {
    try {
      ApiResponse<LodgeReadOneResponse> response = lodgeClient.readOne(lodgeId);
      return response.data().lodge().name();
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private List<LodgeDateReadResponse> getAvailableDates(
      String lodgeId,
      LocalDate checkInDate,
      LocalDate checkOutDate
  ) {
    try {
      Pageable pageable = PageRequest.of(0, 30);
      ApiResponse<SliceResponse<LodgeDateReadResponse>> response =
          lodgeClient.readAll(pageable, lodgeId, checkInDate, checkOutDate);

      return response.data().content();
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private LodgeDataGetResponse applyLodge(
      ReservationCreateRequest request,
      List<LodgeDateReadResponse> dates
  ) {

    Double totalPrice = 0.0;
    List<String> lodgeDateIds = new ArrayList<>();

    for (LodgeDateReadResponse date : dates) {
      LocalDate lodgeDate = date.date().date();

      ReservationStatus status = date.date().status();
      if (isValidReservationDate(request.checkInDate(), request.checkOutDate(), lodgeDate,
          status)) {
        totalPrice += date.date().price();
        lodgeDateIds.add(date.date().id());
      } else {
        throw new DateUnavailableException(ErrorCode.DATE_UNAVAILABLE_EXCEPTION);
      }
    }
    try {
      updateLodgeStatus(lodgeDateIds, "WAITING");
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
    return LodgeDataGetResponse.of(lodgeDateIds, totalPrice);
  }

  private boolean isValidReservationDate(
      LocalDate checkInDate,
      LocalDate checkOutDate,
      LocalDate lodgeDate,
      ReservationStatus status) {
    return !lodgeDate.isBefore(checkInDate)
        && !lodgeDate.isAfter(checkOutDate)
        && ReservationStatus.EMPTY == status;
  }

  private ReservationData applyCouponAndPoint(
      ReservationCreateRequest request,
      String userId,
      Role role,
      ReservationData reservationData
  ) {
    try {
      CouponDataResponse couponResponse = applyCoupon(userId, request.couponId(),
          reservationData.getTotalPrice());
      if (couponResponse != null) {
        reservationData.applyCoupon(couponResponse.reservationCouponId(),
            couponResponse.totalPrice());
      }

      if (request.pointId() != null) {
        String pointHistoryId = applyPoint(request.pointId(), request.point(), userId, role);
        reservationData.applyPoint(pointHistoryId, request.point());
      }
    } catch (Exception e) {
      handleReservationCreationFailure(userId, role, reservationData, e);
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
    return reservationData;
  }

  private void handleReservationCreationFailure(
      String userId,
      Role role,
      ReservationData reservationData,
      Exception e
  ) {
    updateLodgeStatus(reservationData.getLodgeDateIds(), "EMPTY");

    if (reservationData.isCouponApplied()) {
      rollbackReservationCoupon(userId, role, reservationData.getReservationCouponId());
    }

    if (reservationData.isPointApplied()) {
      updatePointStatus(PointStatusRequest.of("상태 변경", "ROLLBACK"),
          reservationData.getPointHistoryId(), userId, role);
    }

    log.error("Error during reservation creation: ", e);
  }

  private CouponDataResponse applyCoupon(String userId, String userCouponId, double lodgePrice) {

    if (userCouponId == null) {
      return null;
    }
    try {
      ApiResponse<ReservationVerifyResponse> response =
          couponClient.verify(userId, FeignVerifyRequest.of(userCouponId, lodgePrice));

      return CouponDataResponse.of(
          response.data().reservationCouponId(),
          response.data().discountedPrice()
      );

    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private String applyPoint(String pointId, Double usePoint, String userId, Role role) {
    try {
      return pointClient.usePoint(
          PointTransactionRequest.of(usePoint), pointId, userId, role).data().historyId();


    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private void completeReservation(Reservation reservation, String userId, Role role) {

    List<ReservationDate> reservationDateList = reservation.getDates();
    List<String> dateIds = reservationDateList.stream()
        .map(ReservationDate::getDateId)
        .toList();

    try {
      updateLodgeStatus(dateIds, "COMPLETE");
      if (!Objects.equals(reservation.getReservationCouponId(), "NOT_APPLIED")) {
        updateCouponStatus(userId, reservation.getReservationCouponId(), "COMPLETED");
      }
      if (!Objects.equals(reservation.getPointHistoryId(), "NOT_APPLIED")) {
        updatePointStatus(
            PointStatusRequest.of(reservation.getReservationId(), "PROCESSED"),
            reservation.getPointHistoryId(), userId, role);
      }
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private void cancelReservation(Reservation reservation, String userId, Role role) {

    List<ReservationDate> reservationDateList = reservation.getDates();
    List<String> dateIds = reservationDateList.stream()
        .map(ReservationDate::getDateId).toList();

    try {
      updateLodgeStatus(dateIds, "EMPTY");
      if (!Objects.equals(reservation.getReservationCouponId(), "NOT_APPLIED")) {
        updateCouponStatus(userId, reservation.getReservationCouponId(), "CANCEL");
      }
      if (!Objects.equals(reservation.getPointHistoryId(), "NOT_APPLIED")) {
        updatePointStatus(
            PointStatusRequest.of(reservation.getReservationId(), "ROLLBACK"),
            reservation.getPointHistoryId(), userId, role);
      }
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private void updateLodgeStatus(List<String> lodgeDateIds, String status) {
    try {
      lodgeClient.updateStatus(LodgeDateUpdateStatusRequest.of(lodgeDateIds, status));
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private void updateCouponStatus(String userId, String couponId, String status) {
    if (couponId != null) {
      try {
        couponClient.confirmReservation(userId, couponId,
            FeignConfirmReservationRequest.of(status));
      } catch (Exception e) {
        throw FeignExceptionUtils.convertToFeignClientException(e);
      }
    }
  }

  private void updatePointStatus(
      PointStatusRequest request,
      String pointHistoryId,
      String userId,
      Role role
  ) {
    try {
      pointClient.updateStatusPoint(request, pointHistoryId, userId, role);
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private void rollbackReservationCoupon(
      String userId,
      Role role,
      String reservationCouponId
  ) {
    try {
      couponClient.rollbackReservationCoupon(userId, role, reservationCouponId);
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private PaymentDataResponse requestPayment(
      Reservation reservation,
      String userId,
      Role role,
      String token,
      ReservationData reservationData
  ) {
    try {
      ApiResponse<Map<String, Object>> response = paymentClient.createPayment(
          PaymentCreateRequest.of(
              reservation.getUserId(),
              reservation.getReservationId(),
              reservation.getTotalPrice(),
              "CARD"),
          userId, role, token
      );
      Map<String, Object> data = response.data();
      ObjectMapper objectMapper = new ObjectMapper();

      PaymentResponse payment = objectMapper.convertValue(data.get("payment"),
          PaymentResponse.class);

      String paymentId = payment.paymentId();
      String paymentUrl = (String) data.get("paymentPageUrl");

      return PaymentDataResponse.of(paymentId, paymentUrl);
    } catch (Exception e) {
      handleReservationCreationFailure(userId, role, reservationData, e);
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private String requestCancelPayment(String reason, String paymentId, String userId, Role role) {
    try {
      ApiResponse<PaymentResponse> paymentResponse = paymentClient.cancelPayment(
          PaymentCancelRequest.of(reason), paymentId, userId, role);
      return paymentResponse.data().status();
    } catch (Exception e) {
      throw FeignExceptionUtils.convertToFeignClientException(e);
    }
  }

  private Reservation findReservationById(String reservationId) {
    return reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));
  }

  private void validateReservationOwnership(Reservation reservation, String userId) {
    if (!reservation.getUserId().equals(userId)) {
      throw new ReservationException(ErrorCode.UNAUTHORIZED_ACCESS);
    }
  }

  private void validateCancellationReason(String reason) {
    if (!"CANCELED".equals(reason)) {
      throw new ReservationException(ErrorCode.INVALID_CANCELLATION_REASON);
    }
  }
}
