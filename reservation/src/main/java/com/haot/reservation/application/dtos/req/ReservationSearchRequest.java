package com.haot.reservation.application.dtos.req;

import java.time.LocalDate;

public record ReservationSearchRequest(

    LocalDate checkInDate,
    LocalDate checkOutDate,
    String reservationStatus

) {

}
