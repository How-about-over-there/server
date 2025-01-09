package com.haot.review.presentation.dto;

import java.util.List;

public record LodgeReadOneResponse(
    LodgeResponse lodge,
    List<LodgeImageResponse> images,
    LodgeRuleResponse rule
) {

}
