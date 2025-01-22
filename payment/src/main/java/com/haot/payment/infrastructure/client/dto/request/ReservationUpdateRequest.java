package com.haot.payment.infrastructure.client.dto.request;

public record ReservationUpdateRequest(
    String paymentId,
    String status

) {

}
