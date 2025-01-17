package com.haot.lodge.presentation.request.lodgedate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record LodgeDateAddRequest(
        @NotBlank String lodgeId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        List<LocalDate> excludeDates
) {
}
