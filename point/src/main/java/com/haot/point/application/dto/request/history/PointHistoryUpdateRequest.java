package com.haot.point.application.dto.request.history;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "[Admin] 포인트 내역 수정 REQUEST DTO")
public record PointHistoryUpdateRequest(
        String userId,
        @Schema(description = "0 이상 입력해야 합니다.")
        @Min(0) Double points,
        @Schema(description = "USE, EARN, CANCEL_USE, CANCEL_EARN, EXPIRE 만 요청 가능합니다.")
        String type,
        String description
) {}
