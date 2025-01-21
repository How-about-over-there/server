package com.haot.reservation.presentation.controller;

import com.haot.reservation.application.dtos.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dtos.req.ReservationSearchRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.reservation.application.service.ReservationService;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.domain.model.ReservationStatus;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ReservationAdmin Management", description = "(관리자) 예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reservations")
public class AdminReservationController {

  private final ReservationService reservationService;

  @Operation(summary = "예약 검색 조회", description = "모든 유저의 예약 내역을 검색할 수 있습니다.")
  @RoleCheck({Role.ADMIN, Role.HOST})
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ApiResponse<Page<ReservationGetResponse>> search(
      ReservationAdminSearchRequest request,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-User-Role", required = true) Role role,
      Pageable pageable
  ) {
    return ApiResponse.SUCCESS(reservationService.search(request, userId, role, pageable));
  }
}
