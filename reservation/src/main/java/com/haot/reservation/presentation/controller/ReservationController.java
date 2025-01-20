package com.haot.reservation.presentation.controller;

import com.haot.reservation.application.dtos.req.ReservationCancelRequest;
import com.haot.reservation.application.dtos.req.ReservationCreateRequest;
import com.haot.reservation.application.dtos.req.ReservationSearchRequest;
import com.haot.reservation.application.dtos.req.ReservationUpdateRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.reservation.application.service.ReservationService;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.common.response.enums.SuccessCode;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Reservation Management", description = "예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  @Operation(summary = "예약 생성", description = "사용자가 예약을 생성할 수 있습니다.")
  @RoleCheck(Role.USER)
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ApiResponse<ReservationGetResponse> createReservation(
      @Valid @RequestBody ReservationCreateRequest reservationCreateRequest,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role
  ) {
    return ApiResponse.SUCCESS(
        reservationService.createReservation(reservationCreateRequest, userId, role));
  }

  @Operation(summary = "예약 단건 조회", description = "사용자가 예약 번호로 조회할 수 있습니다.")
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

  @Operation(summary = "예약 검색 조회", description = "사용자가 예약 내역을 검색할 수 있습니다.")
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

  @Operation(summary = "예약 상태 변경", description = "예약 내역을 성공, 실패로 수정할 수 있습니다.")
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

  @Operation(summary = "예약 취소", description = "생성된 예약을 취소할 수 있습니다.")
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
