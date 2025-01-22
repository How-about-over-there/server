package com.haot.lodge.application.response;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "숙소 목록 조회 요청에 대한 응답")
public record LodgeReadAllResponse(
        LodgeDto lodge,
        List<LodgeImageDto> images
) {
}
