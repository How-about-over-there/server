package com.haot.point.application.dto.request.point;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "포인트 생성 REQUEST DTO")
public record PointCreateRequest(
        String userId,
        @Schema(description = "0 이상 입력해야 합니다.")
        @NotNull @Min(0) Double totalPoints
) {}
