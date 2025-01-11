package com.haot.reservation.presentation.controller;

import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.domain.model.ReservationStatus;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reservations")
public class AdminReservationController {

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{reservationId}")
  public ApiResponse<ReservationGetResponse> getReservation(
      @PathVariable String reservationId
  ) {
    return ApiResponse.success(createDummyReservation());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ApiResponse<ReservationGetResponse> searchReservation() {
    return ApiResponse.success(createDummyReservation());
  }

  private ReservationGetResponse createDummyReservation() {
    return ReservationGetResponse.builder()
        .reservationId(String.valueOf(UUID.randomUUID()))
        .userId(String.valueOf(UUID.randomUUID()))
        .lodgeName("뽀로로")
        .checkInDate(LocalDate.of(2025, 1, 1))
        .checkOutDate(LocalDate.of(2025, 1, 5))
        .numGuests(4)
        .request("~~ 준비해주세요!")
        .totalPrice(350000.0)
        .status(ReservationStatus.PENDING)
        .paymentId("결제 대기중 입니다.")
        .build();
  }
}
