package com.haot.lodge.application.dto;


import com.haot.lodge.domain.model.LodgeRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "숙소 규칙 정보")
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
