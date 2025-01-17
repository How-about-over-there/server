package com.haot.lodge.application.response;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeImageDto;
import com.haot.lodge.application.dto.LodgeRuleDto;
import java.util.List;


public record LodgeReadOneResponse(
        LodgeDto lodge,
        List<LodgeImageDto> images,
        LodgeRuleDto rule
) {
}
