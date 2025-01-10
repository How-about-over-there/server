package com.haot.reservation.infrastructure.dtos.lodge;

public record LodgeRuleResponse(
    String id,
    Integer maxReservationDay,
    Integer maxPersonnel,
    String customization
) {
}
