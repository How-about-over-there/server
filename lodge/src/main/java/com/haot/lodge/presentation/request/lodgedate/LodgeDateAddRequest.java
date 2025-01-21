package com.haot.lodge.presentation.request.lodgedate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Schema(description = "숙소에 예약 받을 날짜를 추가하기 위한 요청")
public record LodgeDateAddRequest(
        @NotBlank String lodgeId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        List<LocalDate> excludeDates
) {
}
