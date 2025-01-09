package com.haot.payment.infrastructure.client.dto.response;

public record PortOneResponse(
        String status,
        String merchantId,
        Amount amount
) {
    public record Amount(
            Integer total
    ) {}
}
