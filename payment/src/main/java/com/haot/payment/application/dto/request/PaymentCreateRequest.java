package com.haot.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "결제 생성 REQUEST DTO")
public record PaymentCreateRequest(
        @NotBlank String userId,
        @NotBlank String reservationId,
        @NotNull @Min(0) Double price,
        @Schema(description = "CARD, TRANSFER 만 요청 가능합니다.")
        @NotNull String method
) {}