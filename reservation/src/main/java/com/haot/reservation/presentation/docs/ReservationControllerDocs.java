package com.haot.reservation.presentation.docs;

import com.haot.reservation.application.dto.req.ReservationCancelRequest;
import com.haot.reservation.application.dto.req.ReservationCreateRequest;
import com.haot.reservation.application.dto.req.ReservationSearchRequest;
import com.haot.reservation.application.dto.req.ReservationUpdateRequest;
import com.haot.reservation.application.dto.res.ReservationGetResponse;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "Reservation Management", description = "예약 API")
public interface ReservationControllerDocs {

  @Operation(summary = "예약 생성", description = "사용자가 예약을 생성할 수 있습니다.")
  ApiResponse<ReservationGetResponse> createReservation(
      ReservationCreateRequest reservationCreateRequest,
      String userId,
      Role role,
      String token
  );

  @Operation(summary = "예약 단건 조회", description = "사용자가 예약 번호로 조회할 수 있습니다.")
  ApiResponse<ReservationGetResponse> getReservation(
      String reservationId,
      String userId,
      Role role
  );

  @Operation(summary = "예약 검색 조회", description = "사용자가 예약 내역을 검색할 수 있습니다.")
  ApiResponse<Page<ReservationGetResponse>> searchReservation(
      ReservationSearchRequest reservationSearchRequest,
      String userId,
      Role role,
      Pageable pageable
  );

  @Operation(summary = "예약 상태 변경", description = "예약 내역을 성공, 실패로 수정할 수 있습니다.")
  ApiResponse<Void> updateReservation(
      ReservationUpdateRequest reservationUpdateRequest,
      String reservationId,
      String userId,
      Role role
  );

  @Operation(summary = "예약 취소", description = "생성된 예약을 취소할 수 있습니다.")
  ApiResponse<Void> cancelReservation(
      String reservationId,
      ReservationCancelRequest reservationCancelRequest,
      String userId,
      Role role
  );
}
