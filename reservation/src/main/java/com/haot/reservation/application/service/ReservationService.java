package com.haot.reservation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.reservation.application.dtos.req.ReservationCreateRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
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
import com.haot.reservation.infrastructure.dtos.payment.PaymentCreateRequest;
import com.haot.reservation.infrastructure.dtos.point.PointStatusRequest;
import com.haot.reservation.infrastructure.dtos.point.PointTransactionRequest;
import com.haot.reservation.infrastructure.enums.ReservationStatus;
import com.haot.submodule.role.Role;
import feign.FeignException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

  private final LodgeClient lodgeClient;
  private final CouponClient couponClient;
  private final PointClient pointClient;
  private final PaymentClient paymentClient;
  private final ReservationRepository reservationRepository;

  @Transactional
  public ReservationGetResponse createReservation(
      ReservationCreateRequest request,
      String userId,
      Role role
  ) throws JsonProcessingException {

    Double totalPrice = 0.0;
    String lodgeName = getLodgeName(request.lodgeId());
    String reservationCouponId = "";
    String paymentId = "";
    String pointHistoryId = "";

    List<LodgeDateReadResponse> availableDates = getAvailableDates(
        request.lodgeId(),
        request.checkInDate(),
        request.checkOutDate()
    );

    LodgeDataGetResponse lodgeDataGetResponse = applyLodge(request, availableDates);
    totalPrice = lodgeDataGetResponse.totalPrice();

    // 예약 상태 업데이트하는 List
    List<String> lodgeDateIds = lodgeDataGetResponse.lodgeDateIds();

    // 쿠폰 적용 가격(totalPrice) 업데이트 및 reservationCouponId 반환
    CouponDataResponse couponResponse = applyCoupon(userId, request.couponId(), totalPrice);
    if (couponResponse != null) {
      reservationCouponId = couponResponse.reservationCouponId();
      totalPrice = couponResponse.totalPrice();
    }

    if (request.pointId() != null) {
      pointHistoryId = applyPoint(request.pointId(), request.point(), userId, role);
      totalPrice -= request.point();
    }

    Reservation reservation = Reservation.createReservation(
        userId,
        lodgeName,
        request.checkInDate(),
        request.checkOutDate(),
        request.numGuests(),
        request.request(),
        totalPrice,
        paymentId,
        reservationCouponId,
        pointHistoryId
    );

    List<ReservationDate> reservationDateList = lodgeDateIds.stream()
        .map(dateId -> ReservationDate.create(reservation, dateId))
        .toList();

    reservation.getDates().addAll(reservationDateList);
    reservationRepository.save(reservation);

    // 결제 요청
    String url = requestPayment(reservation, userId, role);

    return ReservationGetResponse.of(reservation, url);
  }

  private String getLodgeName(String lodgeId) throws JsonProcessingException {
    try {
      ApiResponse<LodgeReadOneResponse> response = lodgeClient.readOne(lodgeId);
      return response.data().lodge().name();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  private List<LodgeDateReadResponse> getAvailableDates(
      String lodgeId,
      LocalDate checkInDate,
      LocalDate checkOutDate
  ) throws JsonProcessingException {
    try {
      Pageable pageable = PageRequest.of(0, 30);
      ApiResponse<SliceResponse<LodgeDateReadResponse>> response =
          lodgeClient.read(pageable, lodgeId, checkInDate, checkOutDate);

      return response.data().content();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
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
    return LodgeDataGetResponse.of(lodgeDateIds, totalPrice);
  }

  // 예약 날짜 유효성 검사 메서드
  private boolean isValidReservationDate(
      LocalDate checkInDate,
      LocalDate checkOutDate,
      LocalDate lodgeDate,
      ReservationStatus status) {
    return !lodgeDate.isBefore(checkInDate) && !lodgeDate.isAfter(checkOutDate)
        && ReservationStatus.EMPTY == status;
  }

  // Lodge date 상태 변경 메서드
  private void updateLodgeStatus(List<String> lodgeDateIds, String status)
      throws JsonProcessingException {
    try {
      lodgeClient.updateStatus(new LodgeDateUpdateStatusRequest(lodgeDateIds, status));
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  // 쿠폰 적용 (totalPrice 적용 및 reservationCouponId 반환 )
  private CouponDataResponse applyCoupon(String userId, String userCouponId, double lodgePrice)
      throws JsonProcessingException {
    if (userCouponId == null) {
      return null;
    }
    try {
      ApiResponse<ReservationVerifyResponse> response = couponClient.verify(
          new FeignVerifyRequest(userCouponId, userId, lodgePrice)
      );

      return CouponDataResponse.of(
          response.data().reservationCouponId(),
          response.data().discountedPrice()
      );
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  // 쿠폰 상태 변경
  private void updateCouponStatus(String couponId, String status) throws JsonProcessingException {
    if (couponId != null) {
      try {
        couponClient.confirmReservation(couponId, new FeignConfirmReservationRequest(status));
      } catch (FeignException e) {
        throw FeignExceptionUtils.parseFeignException(e);
      }
    }
  }

  private String applyPoint(String pointId, Double usePoint, String userId, Role role)
      throws JsonProcessingException {
    try {
      return pointClient.usePoint(new PointTransactionRequest(usePoint, "USE", "포인트 사용"), pointId,
              userId, role)
          .data().historyId();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  private void updatePointStatus(PointStatusRequest request, String pointHistoryId, String userId,
      Role role)
      throws JsonProcessingException {
    try {
      pointClient.updateStatusPoint(request, pointHistoryId, userId, role);
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  public void updateReservation(String status, String reservationId, String userId, Role role)
      throws JsonProcessingException {

    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

    switch (status) {
      case "COMPLETED" -> completeReservation(reservation, userId, role);
      case "CANCELED" -> cancelReservation(reservation, userId, role);
      default -> throw new IllegalArgumentException("Invalid status");
    }
  }

  private void completeReservation(Reservation reservation, String userId, Role role)
      throws JsonProcessingException {

    List<ReservationDate> reservationDateList = reservation.getDates();
    List<String> dateIds = reservationDateList.stream()
        .map(ReservationDate::getDateId)
        .toList();

    PointStatusRequest request = new PointStatusRequest(
        reservation.getReservationId(),
        "PROCESSED",
        "예약 완료"
    );

    try {
      updateLodgeStatus(dateIds, "COMPLETE");
      if (reservation.getReservationCouponId() != null) {
        updateCouponStatus(reservation.getReservationCouponId(), "COMPLETED");
      }
      if (reservation.getPointHistoryId() != null) {
        updatePointStatus(request, reservation.getPointHistoryId(), userId, role);
      }
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  private void cancelReservation(Reservation reservation, String userId, Role role)
      throws JsonProcessingException {

    List<ReservationDate> reservationDateList = reservation.getDates();
    List<String> dateIds = reservationDateList.stream()
        .map(ReservationDate::getDateId)
        .toList();

    PointStatusRequest request = new PointStatusRequest(
        reservation.getReservationId(),
        "ROLLBACK",
        "예약 취소"
    );

    try {
      updateLodgeStatus(dateIds, "EMPTY");
      if (reservation.getReservationCouponId() != null) {
        updateCouponStatus(reservation.getReservationCouponId(), "CANCEL");
      }
      if (reservation.getPointHistoryId() != null) {
        updatePointStatus(request, reservation.getPointHistoryId(), userId, role);
      }
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  private String requestPayment(Reservation reservation, String userId, Role role)
      throws JsonProcessingException {
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
      return (String) data.get("paymentPageUrl");
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }
}
