package com.haot.reservation.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.reservation.application.dtos.ReservationData;
import com.haot.reservation.application.dtos.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dtos.req.ReservationCancelRequest;
import com.haot.reservation.application.dtos.req.ReservationCreateRequest;
import com.haot.reservation.application.dtos.req.ReservationSearchRequest;
import com.haot.reservation.application.dtos.req.ReservationUpdateRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.reservation.common.exceptions.ReservationException;
import com.haot.reservation.common.exceptions.DateUnavailableException;
import com.haot.reservation.common.exceptions.FeignExceptionUtils;
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
import feign.FeignException;
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
      Role role
  ) {

    String lodgeName = getLodgeName(request.lodgeId());

    List<LodgeDateReadResponse> availableDates = getAvailableDates(
        request.lodgeId(),
        request.checkInDate(),
        request.checkOutDate()
    );

    LodgeDataGetResponse lodgeDataGetResponse = applyLodge(request, availableDates);

    List<String> lodgeDateIds = lodgeDataGetResponse.lodgeDateIds();
    ReservationData reservationData = ReservationData.createWithLodgePrice(lodgeDataGetResponse.totalPrice());

    try {
      CouponDataResponse couponResponse = applyCoupon(userId, request.couponId(), reservationData.getTotalPrice());

      if (couponResponse != null) {
        reservationData.applyCoupon(couponResponse.reservationCouponId(), couponResponse.totalPrice());
      }

      if (request.pointId() != null) {
        String pointHistoryId = applyPoint(request.pointId(), request.point(), userId, role);
        reservationData.applyPoint(pointHistoryId, request.point());
      }

    } catch (Exception e) {

      updateLodgeStatus(lodgeDateIds, "EMPTY");

      if (reservationData.isCouponApplied()) {
        rollbackReservationCoupon(userId, role, reservationData.getReservationCouponId());
      }

      if (reservationData.isPointApplied()) {
        updatePointStatus(
            new PointStatusRequest("상태 변경", "ROLLBACK", null),
            reservationData.getPointHistoryId(),
            userId,
            role
        );
      }

      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }

    Reservation reservation = Reservation.createReservation(
        userId,
        lodgeName,
        request.checkInDate(),
        request.checkOutDate(),
        request.numGuests(),
        request.request(),
        reservationData.getTotalPrice(),
        reservationData.getPaymentId(),
        reservationData.getReservationCouponId(),
        reservationData.getPointHistoryId()
    );

    List<ReservationDate> reservationDateList = lodgeDateIds.stream()
        .map(dateId -> ReservationDate.create(reservation, dateId))
        .toList();

    reservation.getDates().addAll(reservationDateList);
    reservationRepository.save(reservation);

    PaymentDataResponse paymentData = requestPayment(reservation, userId, role);

    reservation.getPayment(paymentData.paymentId());

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
    Page<Reservation> reservations = reservationRepository.searchReservation(request, userId, role, pageable);
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
      default -> throw new IllegalArgumentException("Invalid status");
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

    String paymentStatus = requestCancelPayment(request.reason(), reservation.getPaymentId(), userId, role);

    if ("CANCELLED".equals(paymentStatus)) {
      cancelReservation(reservation, userId, role);
      reservation.cancelReservation();
    } else {
      throw new ReservationException(ErrorCode.PAYMENT_ERROR);
    }
  }


  // 숙소 이름 가져오기
  private String getLodgeName(String lodgeId) {
    try {
      ApiResponse<LodgeReadOneResponse> response = lodgeClient.readOne(lodgeId);
      return response.data().lodge().name();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // 예약 가능한 날짜 확인
  private List<LodgeDateReadResponse> getAvailableDates(
      String lodgeId,
      LocalDate checkInDate,
      LocalDate checkOutDate
  ) {
    try {
      Pageable pageable = PageRequest.of(0, 30);
      ApiResponse<SliceResponse<LodgeDateReadResponse>> response =
          lodgeClient.read(pageable, lodgeId, checkInDate, checkOutDate);

      return response.data().content();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // totalPrice 계산 및 예약 날짜 저장
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
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
    return LodgeDataGetResponse.of(lodgeDateIds, totalPrice);
  }

  // 예약 날짜 유효성 검사 메서드
  private boolean isValidReservationDate(
      LocalDate checkInDate,
      LocalDate checkOutDate,
      LocalDate lodgeDate,
      ReservationStatus status) {
    return !lodgeDate.isBefore(checkInDate)
        && !lodgeDate.isAfter(checkOutDate)
        && ReservationStatus.EMPTY == status;
  }

  // 쿠폰 적용 (totalPrice 적용 및 reservationCouponId 반환)
  private CouponDataResponse applyCoupon(String userId, String userCouponId, double lodgePrice) {
    if (userCouponId == null) {
      return null;
    }
    try {
      ApiResponse<ReservationVerifyResponse> response =
          couponClient.verify(new FeignVerifyRequest(userCouponId, userId, lodgePrice)
          );

      return CouponDataResponse.of(
          response.data().reservationCouponId(),
          response.data().discountedPrice()
      );
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // 포인트 적용 및 차감
  private String applyPoint(String pointId, Double usePoint, String userId, Role role) {
    try {
      return pointClient.usePoint(
              new PointTransactionRequest(usePoint, "USE", "포인트 사용"),
              pointId, userId, role
          )
          .data().historyId();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // 예약 완료 시 실행되는 메서드
  private void completeReservation(Reservation reservation, String userId, Role role) {

    List<ReservationDate> reservationDateList = reservation.getDates();
    List<String> dateIds = reservationDateList.stream()
        .map(ReservationDate::getDateId)
        .toList();

    PointStatusRequest request = new PointStatusRequest(
        reservation.getReservationId(),
        "PROCESSED",
        null
    );

    try {
      updateLodgeStatus(dateIds, "COMPLETE");
      if (!Objects.equals(reservation.getReservationCouponId(), "NOT_APPLIED")) {
        updateCouponStatus(reservation.getReservationCouponId(), "COMPLETED");
      }
      if (!Objects.equals(reservation.getPointHistoryId(), "NOT_APPLIED")) {
        updatePointStatus(request, reservation.getPointHistoryId(), userId, role);
      }
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // 예약 취소 시 실행되는 메서드
  private void cancelReservation(Reservation reservation, String userId, Role role) {

    List<ReservationDate> reservationDateList = reservation.getDates();
    List<String> dateIds = reservationDateList.stream()
        .map(ReservationDate::getDateId)
        .toList();

    PointStatusRequest request = new PointStatusRequest(
        reservation.getReservationId(),
        "ROLLBACK",
        null
    );

    try {
      updateLodgeStatus(dateIds, "EMPTY");
      if (!Objects.equals(reservation.getReservationCouponId(), "NOT_APPLIED")) {
        updateCouponStatus(reservation.getReservationCouponId(), "CANCEL");
      }
      if (!Objects.equals(reservation.getPointHistoryId(), "NOT_APPLIED")) {
        updatePointStatus(request, reservation.getPointHistoryId(), userId, role);
      }
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // Lodge date 상태 변경
  private void updateLodgeStatus(List<String> lodgeDateIds, String status) {
    try {
      lodgeClient.updateStatus(new LodgeDateUpdateStatusRequest(lodgeDateIds, status));
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // 쿠폰 상태 변경
  private void updateCouponStatus(
      String couponId,
      String status
  ) {
    if (couponId != null) {
      try {
        couponClient.confirmReservation(couponId, new FeignConfirmReservationRequest(status));
      } catch (FeignException e) {
        throw FeignExceptionUtils.parseFeignException(e);
      } catch (Exception e) {
        throw new ReservationException(ErrorCode.GENERAL_ERROR);
      }
    }
  }

  // 포인트 상태 변경
  private void updatePointStatus(
      PointStatusRequest request,
      String pointHistoryId,
      String userId,
      Role role
  ) {
    try {
      pointClient.updateStatusPoint(request, pointHistoryId, userId, role);
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  private void rollbackReservationCoupon(
      String userId,
      Role role,
      String reservationCouponId
  ) {
    try {
      couponClient.rollbackReservationCoupon(userId, role, reservationCouponId);
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  // 결제 요청
  private PaymentDataResponse requestPayment(
      Reservation reservation,
      String userId,
      Role role
  ) {
    try {
      ApiResponse<Map<String, Object>> response = paymentClient.createPayment(
          new PaymentCreateRequest(
              reservation.getUserId(),
              reservation.getReservationId(),
              reservation.getTotalPrice(),
              "CARD"),
          userId,
          role
      );
      Map<String, Object> data = response.data();
      ObjectMapper objectMapper = new ObjectMapper();

      PaymentResponse payment = objectMapper.convertValue(data.get("payment"),
          PaymentResponse.class);

      String paymentId = payment.paymentId();
      String paymentUrl = (String) data.get("paymentPageUrl");

      return PaymentDataResponse.of(paymentId, paymentUrl);
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
    }
  }

  private String requestCancelPayment(String reason, String paymentId, String userId, Role role) {
    try {
      ApiResponse<PaymentResponse> paymentResponse = paymentClient.cancelPayment(new PaymentCancelRequest(reason), paymentId, userId, role);
      return  paymentResponse.data().status();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    } catch (Exception e) {
      throw new ReservationException(ErrorCode.GENERAL_ERROR);
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
