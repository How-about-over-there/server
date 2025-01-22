package com.haot.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "결제 수정 요청 DTO")
public record PaymentUpdateRequest (
        @Min(0) Double price,
        String method,
        String status
) {}
