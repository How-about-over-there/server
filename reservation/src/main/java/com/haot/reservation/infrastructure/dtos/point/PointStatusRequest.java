package com.haot.reservation.infrastructure.dtos.point;

import jakarta.validation.constraints.NotBlank;

public record PointStatusRequest(
    @NotBlank String contextId,
    @NotBlank String status,
    String cancelType
) {
}
