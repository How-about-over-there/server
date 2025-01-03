package com.haot.point.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PointStatusRequest(
        @NotBlank String status
) {
}
