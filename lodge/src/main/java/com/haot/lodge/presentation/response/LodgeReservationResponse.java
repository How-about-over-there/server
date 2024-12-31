package com.haot.lodge.presentation.response;

import com.haot.lodge.application.response.LodgeResponse;
import java.time.LocalDate;
import java.util.List;

public record LodgeReservationResponse(
        LodgeResponse lodge,
        List<LocalDate> dates
) {
}
