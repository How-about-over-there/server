package com.haot.lodge.presentation.request.lodgedate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@NotNull
@Schema(description = "숙소 날짜의 상세 정보 수정 요청. 수정이 필요한 값만 포함해서 요청합니다.")
public record LodgeDateUpdateRequest(
        Double price
) {
}
