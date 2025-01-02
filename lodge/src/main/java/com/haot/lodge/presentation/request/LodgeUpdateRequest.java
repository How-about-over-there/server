package com.haot.lodge.presentation.request;


public record LodgeUpdateRequest(
        String name,
        String description,
        String address,
        Integer term,
        Double basicPrice
) {
}
