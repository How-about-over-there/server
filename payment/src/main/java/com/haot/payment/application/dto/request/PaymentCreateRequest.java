package com.haot.payment.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentCreateRequest(
        @NotBlank String userId,
        @NotBlank String reservationId,
        @NotNull @Min(0) Double price,
        @NotNull String method
) {}