package com.haot.lodge.application.response;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeImageDto;
import com.haot.lodge.application.dto.LodgeRuleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "숙소 단 건 상세 조회 요청에 대한 응답")
public record LodgeReadOneResponse(
        LodgeDto lodge,
        List<LodgeImageDto> images,
        LodgeRuleDto rule
) {
}
