package com.haot.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "결제 생성 요청 DTO")
public record PaymentCreateRequest(
        @NotBlank String userId,
        @NotBlank String reservationId,
        @NotNull @Min(0) Double price,
        @NotNull String method
) {}