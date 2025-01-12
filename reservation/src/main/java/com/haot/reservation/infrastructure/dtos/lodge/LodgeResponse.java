package com.haot.reservation.infrastructure.dtos.lodge;

public record LodgeResponse(
    String id,
    String hostId,
    String name,
    String description,
    String address,
    Integer term,
    Double basicPrice
) {
}
