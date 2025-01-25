package com.haot.reservation.presentation.docs;

import com.haot.reservation.application.dto.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dto.res.ReservationGetResponse;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "ReservationAdmin Management", description = "(관리자) 예약 API")
public interface AdminReservationControllerDocs {

  @Operation(summary = "예약 검색 조회", description = "모든 유저의 예약 내역을 검색할 수 있습니다.")
  ApiResponse<Page<ReservationGetResponse>> search(
      ReservationAdminSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  );
}
