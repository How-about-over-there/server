package com.haot.point.application.dto.request.history;

import jakarta.validation.constraints.Min;

public record PointHistoryUpdateRequest(
        String userId,
        @Min(0) Double points,
        String type,
        String description
) {}
