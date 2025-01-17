package com.haot.lodge.application.dto;

import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import java.time.LocalDate;

public record LodgeDateDto(
        String id,
        LocalDate date,
        Double price,
        ReservationStatus status
){
    public static LodgeDateDto from(LodgeDate lodgeDate) {
        return new LodgeDateDto(
                lodgeDate.getId(),
                lodgeDate.getDate(),
                lodgeDate.getPrice(),
                lodgeDate.getStatus()
        );
    }
}
