package com.haot.reservation.infrastructure.dtos.point;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PointTransactionRequest(
    @NotNull @Min(0) Double points,
    @NotBlank String type,
    String contextId
) {}
