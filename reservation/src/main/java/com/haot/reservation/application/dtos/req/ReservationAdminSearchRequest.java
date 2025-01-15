package com.haot.reservation.application.dtos.req;

import java.time.LocalDate;

public record ReservationAdminSearchRequest(

    String reservationId,
    String userId,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    String reservationStatus
) {

}
