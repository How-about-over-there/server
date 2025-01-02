package com.haot.payment.application.dto.response;

public record PaymentResponse(
        String paymentId,
        String userId,
        String reservationId,
        String impUid,
        String merchantUid,
        double price,
        double finalPrice,
        String method,
        String status
) {}