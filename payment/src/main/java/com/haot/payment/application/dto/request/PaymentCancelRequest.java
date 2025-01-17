package com.haot.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "결제 취소 요청 DTO")
public record PaymentCancelRequest(
        @NotBlank String reason
        // 일단 본인이 결제한 결제 수단으로만 환불 가능
) {}
