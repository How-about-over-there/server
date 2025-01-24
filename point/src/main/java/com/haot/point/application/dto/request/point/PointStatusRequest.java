package com.haot.point.application.dto.request.point;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "포인트 상태 변경 REQUEST DTO")
public record PointStatusRequest(
        @Schema(description = "포인트 상태 변경이 일어난 이유")
        @NotBlank String contextId,
        @Schema(description = "PROCESSED, ROLLBACK 만 요청 가능합니다.")
        @NotBlank String status,
        @Schema(description = "CANCEL_USE, CANCEL_EARN 만 요청 가능합니다.")
        String cancelType
) {
}
