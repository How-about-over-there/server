package com.haot.point.application.dto.request.point;

import jakarta.validation.constraints.NotBlank;

public record PointStatusRequest(
        @NotBlank String contextId,
        @NotBlank String status,
        String cancelType
) {
}
