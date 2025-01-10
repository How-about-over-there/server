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
  ) throws JsonProcessingException {

    Double totalPrice = 0.0;
    String lodgeName = getLodgeName(request.lodgeId());
    String reservationCouponId = "";
    String paymentId = "";
    String pointHistoryId = "";

    // 예약 가능한 날짜 확인
    List<LodgeDateReadResponse> availableDates = getAvailableDates(
        request.lodgeId(),
        request.checkInDate(),
        request.checkOutDate()
    );

    // 숙소 적용 및 totalPrice 계산
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

    // 포인트 적용 및 차감 가격(totalPrice) 업데이트
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

    // 예약 날짜 생성 및 저장
    reservation.getDates().addAll(reservationDateList);
    reservationRepository.save(reservation);

    // 결제 요청 URL 반환
    String url = requestPayment(reservation, userId, role);

    return ReservationGetResponse.of(reservation, url);
  }

  // 숙소 이름 가져오기
  private String getLodgeName(String lodgeId) throws JsonProcessingException {
    try {
      ApiResponse<LodgeReadOneResponse> response = lodgeClient.readOne(lodgeId);
      return response.data().lodge().name();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  // 예약 가능한 날짜 확인
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

  // 쿠폰 적용 (totalPrice 적용 및 reservationCouponId 반환)
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

  // 포인트 적용 및 차감
  private String applyPoint(String pointId, Double usePoint, String userId, Role role)
      throws JsonProcessingException {
    try {
      return pointClient.usePoint(
          new PointTransactionRequest(
              usePoint,
              "USE",
              "포인트 사용"),
              pointId,
              userId,
              role
          )
          .data().historyId();
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  // 예약 완료 시 실행되는 메서드
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

  // 예약 완료 시 실행되는 메서드
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

  // Lodge date 상태 변경
  private void updateLodgeStatus(List<String> lodgeDateIds, String status)
      throws JsonProcessingException {
    try {
      lodgeClient.updateStatus(new LodgeDateUpdateStatusRequest(lodgeDateIds, status));
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

  // 포인트 상태 변경
  private void updatePointStatus(PointStatusRequest request, String pointHistoryId, String userId,
      Role role)
      throws JsonProcessingException {
    try {
      pointClient.updateStatusPoint(request, pointHistoryId, userId, role);
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  // 결제 요청
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

  // 업데이트 로직에 사용될 메서드
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
}
