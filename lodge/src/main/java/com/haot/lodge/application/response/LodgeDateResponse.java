package com.haot.lodge.application.response;

import com.haot.lodge.domain.model.enums.ReservationStatus;
import java.time.LocalDate;
import lombok.Builder;


@Builder
public record LodgeDateResponse(
        String id,
        LocalDate date,
        Double price,
        ReservationStatus status
) {
}
