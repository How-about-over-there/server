package com.haot.lodge.presentation.request;

import java.time.LocalDate;
import java.util.List;

public record LodgeReservationRequest(
        List<LocalDate> dates
) {
}
