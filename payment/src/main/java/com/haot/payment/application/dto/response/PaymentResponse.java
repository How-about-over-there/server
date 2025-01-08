package com.haot.payment.application.dto.response;

import com.haot.payment.domain.model.Payment;

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
    public static PaymentResponse of(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),
                payment.getReservationId(),
                payment.getMerchantId(),
                payment.getPrice(),
                payment.getFinalPrice(),
                payment.getMethod().toString(),
                payment.getStatus().toString()
        );
    }
}