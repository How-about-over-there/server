package com.haot.lodge.application.response;


import lombok.Builder;

@Builder
public record LodgeRuleResponse(
        String id,
        Integer maxReservationDay,
        Integer maxPersonnel,
        String customization
) {
}
