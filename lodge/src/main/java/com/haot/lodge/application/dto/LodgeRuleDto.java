package com.haot.lodge.application.dto;


import com.haot.lodge.domain.model.LodgeRule;
import lombok.Builder;

@Builder
public record LodgeRuleDto(
        String id,
        Integer maxReservationDay,
        Integer maxPersonnel,
        String customization
) {
    public static LodgeRuleDto from(LodgeRule rule) {
        return new LodgeRuleDto(
                rule.getId(),
                rule.getMaxReservationDay(),
                rule.getMaxPersonnel(),
                rule.getCustomization()
        );
    }
}
