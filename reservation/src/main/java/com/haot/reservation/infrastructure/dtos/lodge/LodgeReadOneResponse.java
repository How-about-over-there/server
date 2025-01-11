package com.haot.reservation.infrastructure.dtos.lodge;

import java.util.List;

public record LodgeReadOneResponse(
    LodgeResponse lodge,
    List<LodgeImageResponse> images,
    LodgeRuleResponse rule
) {
}
