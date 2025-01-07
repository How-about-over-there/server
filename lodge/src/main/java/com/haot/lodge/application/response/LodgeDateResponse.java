package com.haot.lodge.application.response;

import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import java.time.LocalDate;

public record LodgeDateResponse(
        String id,
        LocalDate date,
        Double price,
        ReservationStatus status
){
    public static LodgeDateResponse from(LodgeDate lodgeDate) {
        return new LodgeDateResponse(
                lodgeDate.getId(),
                lodgeDate.getDate(),
                lodgeDate.getPrice(),
                lodgeDate.getStatus()
        );
    }
}
