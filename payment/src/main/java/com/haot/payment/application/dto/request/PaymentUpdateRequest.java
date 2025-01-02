package com.haot.payment.application.dto.request;

public record PaymentUpdateRequest (
        double price,
        String method,
        String status
) {}
