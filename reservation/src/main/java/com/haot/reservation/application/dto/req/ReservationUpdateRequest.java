package com.haot.reservation.application.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "예약 업데이트 요청 정보")
public record ReservationUpdateRequest(
    @Schema(description = "결제 ID", example = "payment789")
    String paymentId,

    @Schema(description = "업데이트할 상태", example = "COMPLETED")
    String status
) { }
