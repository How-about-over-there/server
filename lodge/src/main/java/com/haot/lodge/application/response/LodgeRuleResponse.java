package com.haot.lodge.application.response;


import com.haot.lodge.domain.model.LodgeRule;
import lombok.Builder;

@Builder
public record LodgeRuleResponse(
        String id,
        Integer maxReservationDay,
        Integer maxPersonnel,
        String customization
) {
    public static LodgeRuleResponse from(LodgeRule rule) {
        return new LodgeRuleResponse(
                rule.getId(),
                rule.getMaxReservationDay(),
                rule.getMaxPersonnel(),
                rule.getCustomization()
        );
    }
}
