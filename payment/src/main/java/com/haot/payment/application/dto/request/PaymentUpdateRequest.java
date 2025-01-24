package com.haot.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "결제 수정 REQUEST DTO")
public record PaymentUpdateRequest (
        @Schema(description = "0 이상 입력해야 합니다.")
        @Min(0) Double price,
        @Schema(description = "CARD, TRANSFER 만 요청 가능합니다.")
        String method,
        String status
) {}
