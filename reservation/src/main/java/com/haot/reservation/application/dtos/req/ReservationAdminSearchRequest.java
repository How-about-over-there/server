package com.haot.reservation.application.dtos.req;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;


@Schema(description = "관리자 예약 검색 요청 정보")
public record ReservationAdminSearchRequest(

    @Schema(description = "예약 ID", example = "reservation123")
    String reservationId,

    @Schema(description = "사용자 ID", example = "user456")
    String userId,

    @Schema(description = "체크인 날짜", example = "2025-02-01")
    LocalDate checkInDate,

    @Schema(description = "체크아웃 날짜", example = "2025-02-05")
    LocalDate checkOutDate,

    @Schema(description = "예약 상태", example = "PENDING")
    String reservationStatus
) {

}
