package com.haot.point.application.dto.request.history;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "[Admin] 포인트 내역 생성 REQUEST DTO")
public record PointHistoryCreateRequest(
        @NotBlank String pointId,
        @Schema(description = "0 이상 입력해야 합니다.")
        @NotNull @Min(0) Double points,
        @Schema(description = "USE, EARN, CANCEL_USE, CANCEL_EARN, EXPIRE 만 요청 가능합니다.")
        @NotBlank String type,
        @NotBlank String description
) {}
