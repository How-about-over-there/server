package com.haot.lodge.presentation.request.lodge;

import jakarta.validation.constraints.NotNull;

@NotNull
public record LodgeUpdateRequest(
        String name,
        String description,
        String address,
        Integer term,
        Double basicPrice
) {
}
