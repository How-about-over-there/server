package com.haot.review.presentation.dto;

public record LodgeRuleResponse(
    String id,
    Integer maxReservationDay,
    Integer maxPersonnel,
    String customization
) {
}
