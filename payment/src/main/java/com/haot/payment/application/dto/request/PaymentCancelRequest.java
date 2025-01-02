package com.haot.payment.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentCancelRequest(
        @NotNull @Min(0) double price,
        @NotBlank String method
) {}
