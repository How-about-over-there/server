package com.haot.reservation.infrastructure.dtos.lodge;

import com.haot.reservation.domain.model.ReservationStatus;
import java.time.LocalDate;

public record LodgeDateResponse(
    String id,
    LocalDate date,
    Double price,
    ReservationStatus status
){
}
