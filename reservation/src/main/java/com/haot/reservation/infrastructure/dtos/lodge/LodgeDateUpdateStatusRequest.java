package com.haot.reservation.infrastructure.dtos.lodge;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LodgeDateUpdateStatusRequest(
    @NotNull List<String> lodgeDateIds,
    @NotNull String status
) {
}
