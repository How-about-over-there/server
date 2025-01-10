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
import com.haot.reservation.domain.repository.ReservationDateRepository;
import com.haot.reservation.domain.repository.ReservationRepository;
import com.haot.reservation.infrastructure.client.CouponClient;
import com.haot.reservation.infrastructure.client.LodgeClient;
import com.haot.reservation.infrastructure.dtos.CouponDataResponse;
import com.haot.reservation.infrastructure.dtos.LodgeDataGetResponse;
import com.haot.reservation.infrastructure.dtos.coupon.FeignConfirmReservationRequest;
import com.haot.reservation.infrastructure.dtos.coupon.FeignVerifyRequest;
import com.haot.reservation.infrastructure.dtos.coupon.ReservationVerifyResponse;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeDateReadResponse;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeDateUpdateStatusRequest;
import com.haot.reservation.infrastructure.dtos.lodge.LodgeReadOneResponse;
import com.haot.reservation.infrastructure.enums.ReservationStatus;
import feign.FeignException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
  private final ReservationRepository reservationRepository;
  private final ReservationDateRepository reservationDateRepository;

  @Transactional
  public ReservationGetResponse createReservation(
      ReservationCreateRequest request,
      String userId,
      String role
  ) throws JsonProcessingException {

    Double totalPrice = 0.0;
    String lodgeName = getLodgeName(request.lodgeId());
    String reservationCouponId = "";
    String paymentId = "";

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

    Reservation reservation = Reservation.createReservation(
        userId,
        lodgeName,
        request.checkInDate(),
        request.checkOutDate(),
        request.numGuests(),
        request.request(),
        totalPrice,
        request.pointId(),
        paymentId,
        reservationCouponId
    );

    List<ReservationDate> reservationDateList = lodgeDateIds.stream()
        .map(dateId -> ReservationDate.create(reservation, dateId))
        .toList();

    reservation.getDates().addAll(reservationDateList);

    reservationRepository.save(reservation);
    reservationDateRepository.saveAll(reservationDateList);

    return ReservationGetResponse.of(reservation);
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
          new FeignVerifyRequest( userCouponId, userId, lodgePrice)
      );

      return CouponDataResponse.of(
          response.data().reservationCouponId(),
          response.data().discountedPrice()
      );
    } catch (FeignException e) {
      throw FeignExceptionUtils.parseFeignException(e);
    }
  }

  private void updateCouponStatus(String couponId, String status) throws JsonProcessingException {
    if (couponId != null) {
      try {
        couponClient.confirmReservation(couponId, new FeignConfirmReservationRequest(status));
      } catch (FeignException e) {
        throw FeignExceptionUtils.parseFeignException(e);
      }
    }
  }
}
