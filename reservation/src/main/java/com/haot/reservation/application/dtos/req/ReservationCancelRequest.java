package com.haot.reservation.application.dtos.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "예약 취소 요청 정보")
public record ReservationCancelRequest(
    @Schema(description = "취소 사유", example = "개인 사정으로 취소합니다.")
    @NotBlank
    String reason
) { }
