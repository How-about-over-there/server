package com.haot.lodge.presentation.request;

import jakarta.validation.constraints.NotNull;

@NotNull
public record LodgeDateUpdateRequest(
        Double price
) {
}
