package com.haot.review.infrastructure.dto;

public record LodgeRuleResponse(
    String id,
    Integer maxReservationDay,
    Integer maxPersonnel,
    String customization
) {
}
