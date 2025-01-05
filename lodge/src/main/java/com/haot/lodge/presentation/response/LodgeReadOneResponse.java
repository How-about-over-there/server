package com.haot.lodge.presentation.response;

import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.LodgeImageResponse;
import com.haot.lodge.application.response.LodgeRuleResponse;
import java.util.List;
import lombok.Builder;


@Builder
public record LodgeReadOneResponse(
        LodgeResponse lodge,
        List<LodgeImageResponse> images,
        LodgeRuleResponse rule
) {
}
