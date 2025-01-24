package com.haot.reservation.presentation.controller;

import com.haot.reservation.application.dto.req.ReservationCancelRequest;
import com.haot.reservation.application.dto.req.ReservationCreateRequest;
import com.haot.reservation.application.dto.req.ReservationSearchRequest;
import com.haot.reservation.application.dto.req.ReservationUpdateRequest;
import com.haot.reservation.application.dto.res.ReservationGetResponse;
import com.haot.reservation.application.service.ReservationService;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.common.response.enums.SuccessCode;
import com.haot.reservation.presentation.docs.ReservationControllerDocs;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ReservationController implements ReservationControllerDocs {

  private final ReservationService reservationService;

  @RoleCheck(Role.USER)
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ApiResponse<ReservationGetResponse> createReservation(
      @Valid @RequestBody ReservationCreateRequest reservationCreateRequest,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role,
      @RequestHeader("Authorization") String token
  ) {
    return ApiResponse.SUCCESS(
        reservationService.createReservation(reservationCreateRequest, userId, role, token));
  }

  @RoleCheck(Role.USER)
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{reservationId}")
  public ApiResponse<ReservationGetResponse> getReservation(
      @PathVariable String reservationId,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role
  ) {
    return ApiResponse.SUCCESS(reservationService.getReservation(reservationId, userId, role));
  }

  @RoleCheck(Role.USER)
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ApiResponse<Page<ReservationGetResponse>> searchReservation(
      ReservationSearchRequest reservationSearchRequest,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role,
      Pageable pageable
  ) {
    return ApiResponse.SUCCESS(
        reservationService.searchReservation(reservationSearchRequest, userId, role, pageable));
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
    return ApiResponse.SUCCESS(SuccessCode.UPDATE_RESERVATION_SUCCESS);
  }

  @RoleCheck(Role.USER)
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{reservationId}")
  public ApiResponse<Void> cancelReservation(
      @PathVariable String reservationId,
      @Valid @RequestBody ReservationCancelRequest reservationCancelRequest,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role
  ) {
    reservationService.cancelReservation(reservationId, reservationCancelRequest, userId, role);
    return ApiResponse.SUCCESS(SuccessCode.CANCEL_RESERVATION_SUCCESS);
  }
}
