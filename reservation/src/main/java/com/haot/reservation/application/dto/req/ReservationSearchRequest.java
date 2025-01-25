package com.haot.reservation.application.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "예약 검색 요청 정보")
public record ReservationSearchRequest(
    @Schema(description = "체크인 날짜", example = "2025-02-01")
    LocalDate checkInDate,

    @Schema(description = "체크아웃 날짜", example = "2025-02-05")
    LocalDate checkOutDate,

    @Schema(description = "예약 상태", example = "CANCELED")
    String reservationStatus
) { }
