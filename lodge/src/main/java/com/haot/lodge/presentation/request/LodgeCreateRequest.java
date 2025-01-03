package com.haot.lodge.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record LodgeCreateRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String address,
        @NotNull Integer term,
        @NotNull Double basicPrice,
        @NotNull MultipartFile image,
        @NotNull String imageTitle,
        @NotNull String imageDescription,
        @NotNull Integer maxReservationDay,
        @NotNull Integer maxPersonnel,
        String customization,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        List<LocalDate> excludeDates
) {

}
