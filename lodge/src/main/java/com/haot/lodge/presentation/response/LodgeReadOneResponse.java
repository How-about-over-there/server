package com.haot.lodge.presentation.response;

import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.lodgeImageResponse;
import com.haot.lodge.application.response.LodgeRuleResponse;
import java.util.List;
import lombok.Builder;


@Builder
public record LodgeReadOneResponse(
        LodgeResponse lodge,
        List<lodgeImageResponse> images,
        LodgeRuleResponse rule
) {
}
