package com.haot.lodge.presentation.request.lodgedate;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record LodgeDateSearchParams(
        @NotBlank String lodgeId,
        LocalDate start,
        LocalDate end
) {
}
