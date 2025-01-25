package com.haot.reservation.application.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "예약 생성 요청 정보")
public record ReservationCreateRequest(
    @Schema(description = "체크인 날짜", example = "2025-02-01")
    @NotNull
    LocalDate checkInDate,

    @Schema(description = "체크아웃 날짜", example = "2025-02-05")
    @NotNull
    LocalDate checkOutDate,

    @Schema(description = "투숙객 수", example = "2")
    @NotNull
    Integer numGuests,

    @Schema(description = "사용자 요청사항", example = "조용한 방으로 부탁드립니다.")
    String request,

    @Schema(description = "숙소 ID", example = "lodge123")
    @NotBlank
    String lodgeId,

    @Schema(description = "포인트 ID", example = "point456")
    String pointId,

    @Schema(description = "사용할 포인트", example = "5000.0")
    Double point,

    @Schema(description = "쿠폰 ID", example = "coupon789")
    String couponId
) { }
