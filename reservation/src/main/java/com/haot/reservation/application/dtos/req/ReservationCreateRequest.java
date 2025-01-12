package com.haot.reservation.application.dtos.req;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ReservationCreateRequest(
    @NotBlank
    LocalDate checkInDate,
    @NotBlank
    LocalDate checkOutDate,
    @NotBlank
    Integer numGuests,
    String request,
    @NotBlank
    String lodgeId,
    String pointId,
    Double point,
    String couponId
) {

}
