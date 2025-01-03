package com.haot.point.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PointHistoryCreateRequest(
        @NotBlank String pointId,
        @NotNull @Min(0) Double points,
        @NotBlank String type,
        @NotBlank String description
) {}
