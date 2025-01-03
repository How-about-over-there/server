package com.haot.point.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointCreateRequest(
        String userId,
        @NotNull @Min(0) Double totalPoints
) {}
