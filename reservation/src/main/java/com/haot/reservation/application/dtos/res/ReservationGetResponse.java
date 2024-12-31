package com.haot.reservation.application.dtos.res;

import com.haot.reservation.domain.model.ReservationStatus;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ReservationGetResponse(
    String reservationId,
    String userId,
    String lodgeName,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    Integer numGuests,
    String request,
    Integer totalPrice,
    ReservationStatus status,
    String paymentId

) {

}
