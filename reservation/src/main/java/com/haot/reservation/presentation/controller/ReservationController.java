package com.haot.reservation.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.reservation.application.dtos.req.ReservationCreateRequest;
import com.haot.reservation.application.dtos.req.ReservationUpdateRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.reservation.application.service.ReservationService;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.domain.model.ReservationStatus;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  @RoleCheck(Role.USER)
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ApiResponse<ReservationGetResponse> createReservation(
      @RequestBody ReservationCreateRequest reservationCreateRequest,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role
  ) {
    return ApiResponse.success(
        reservationService.createReservation(reservationCreateRequest, userId, role));
  }

  @RoleCheck(Role.USER)
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{reservationId}")
  public ApiResponse<ReservationGetResponse> getReservation(
      @PathVariable String reservationId,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role
  ) {
    return ApiResponse.success(reservationService.getReservation(reservationId, userId, role));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ApiResponse<ReservationGetResponse> searchReservation() {
    return ApiResponse.success(createDummyReservation());
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{reservationId}")
  public ApiResponse<Void> updateReservation(
      @RequestBody ReservationUpdateRequest reservationUpdateRequest,
      @PathVariable String reservationId,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role
  ) {
    reservationService.updateReservation(reservationUpdateRequest, reservationId, userId, role);
    return ApiResponse.success();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{reservationId}")
  public ApiResponse<Void> deleteReservation(
      @PathVariable String reservationId
  ) {
    return ApiResponse.success();
  }

  private ReservationGetResponse createDummyReservation() {
    return ReservationGetResponse.builder()
        .reservationId(String.valueOf(UUID.randomUUID()))
        .userId(String.valueOf(UUID.randomUUID()))
        .lodgeName("엄청난 숙소")
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
