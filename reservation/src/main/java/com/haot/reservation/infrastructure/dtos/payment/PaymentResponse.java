package com.haot.reservation.infrastructure.dtos.payment;

public record PaymentResponse(
    String paymentId,
    String userId,
    String reservationId,
    String merchantId,
    Double price,
    Double finalPrice,
    String method,
    String status
) {
}
