package com.haot.review.infrastructure.dto;

import java.util.List;

public record LodgeReadOneResponse(
    LodgeResponse lodge,
    List<LodgeImageResponse> images,
    LodgeRuleResponse rule
) {

}
