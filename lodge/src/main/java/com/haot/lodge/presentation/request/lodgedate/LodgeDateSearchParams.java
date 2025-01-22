package com.haot.lodge.presentation.request.lodgedate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;


@Schema(description = "숙소 날짜 목록을 검색을 위한 파라미터")
public record LodgeDateSearchParams(
        @NotBlank String lodgeId,
        LocalDate start,
        LocalDate end
) {
}
