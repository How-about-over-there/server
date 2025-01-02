package com.haot.payment.application.dto.request;

public record PaymentUpdateRequest (
        Double price,
        String method,
        String status
) {}
