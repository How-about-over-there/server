package com.haot.reservation.presentation.controller;

import com.haot.reservation.application.dto.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dto.res.ReservationGetResponse;
import com.haot.reservation.application.service.ReservationService;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.presentation.docs.AdminReservationControllerDocs;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reservations")
public class AdminReservationController implements AdminReservationControllerDocs {

  private final ReservationService reservationService;

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
