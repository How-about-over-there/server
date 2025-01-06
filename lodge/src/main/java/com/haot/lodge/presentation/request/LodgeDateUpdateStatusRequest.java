package com.haot.lodge.presentation.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LodgeDateUpdateStatusRequest(
        @NotNull List<String> lodgeDateIds,
        @NotNull String status
) {
}
