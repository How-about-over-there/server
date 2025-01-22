package com.haot.lodge.application.response;

import com.haot.lodge.application.dto.LodgeDto;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "숙소 생성 요청에 대한 응답")
public record LodgeCreateResponse(
        LodgeDto lodge
) {
}
