package com.haot.lodge.presentation.request;


import com.haot.lodge.domain.model.enums.ReservationStatus;

public record LodgeUpdateRequest(
        String id,
        String date,
        Double price,
        ReservationStatus status
) {
}
