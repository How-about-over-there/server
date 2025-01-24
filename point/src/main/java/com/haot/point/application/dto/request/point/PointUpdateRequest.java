package com.haot.point.application.dto.request.point;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "[Admin] 포인트 수정 REQUEST DTO")
public record PointUpdateRequest(
        String userId,
        @Schema(description = "0 이상 입력해야 합니다.")
        @Min(0) Double totalPoints
){}
