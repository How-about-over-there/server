package com.haot.lodge.application.dto;

import com.haot.lodge.presentation.request.lodge.LodgeSearchParams;
import java.time.LocalDate;

public record LodgeSearchCriteria(
        String hostId,
        String name,
        String address,
        Integer maxReservationDay,
        Integer maxPersonnel,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
    public static LodgeSearchCriteria of(LodgeSearchParams params, boolean onlyCheckIn) {
        return new LodgeSearchCriteria(
                params.hostId(),
                params.name(),
                params.address(),
                params.maxReservationDay(),
                params.maxPersonnel(),
                params.checkInDate(),
                (onlyCheckIn) ? params.checkInDate().plusDays(1) : null
        );
    }
}
