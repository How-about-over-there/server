package com.haot.point.application.dto.request.point;

import jakarta.validation.constraints.Min;

public record PointUpdateRequest(
        String userId,
        @Min(0) Double totalPoints
){}
