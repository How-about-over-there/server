package com.haot.lodge.presentation.request.lodge;

import java.time.LocalDate;

public record LodgeSearchParams(
        String hostId,
        String name,
        String address,
        Integer maxReservationDay,
        Integer maxPersonnel,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
