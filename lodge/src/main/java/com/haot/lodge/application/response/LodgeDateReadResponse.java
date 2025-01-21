package com.haot.lodge.application.response;

import com.haot.lodge.application.dto.LodgeDateDto;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "날짜 조회 요청에 대한 응답")
public record LodgeDateReadResponse(
        LodgeDateDto date
) {
}
