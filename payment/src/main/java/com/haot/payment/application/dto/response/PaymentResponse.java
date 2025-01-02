package com.haot.payment.application.dto.response;

public record PaymentResponse(
        String paymentId,
        String userId,
        String reservationId,
        String impUid,
        String merchantUid,
        Double price,
        Double finalPrice,
        String method,
        String status
) {}