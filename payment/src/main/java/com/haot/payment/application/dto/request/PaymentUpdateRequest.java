package com.haot.payment.application.dto.request;

import jakarta.validation.constraints.Min;

public record PaymentUpdateRequest (
        @Min(0) Double price,
        String method,
        String status
) {}
