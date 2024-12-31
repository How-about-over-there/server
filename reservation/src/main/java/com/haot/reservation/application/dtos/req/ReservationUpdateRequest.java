package com.haot.reservation.application.dtos.req;

public record ReservationUpdateRequest(
    String paymentId,
    String status

) {

}
