package com.haot.payment.infrastructure.client.dto.response;

public record PortOneCancelResponse(
        PaymentCancellation cancellation
) {
    public record PaymentCancellation(
            String status,
            Integer totalAmount,
            String reason
    ) {}
}
