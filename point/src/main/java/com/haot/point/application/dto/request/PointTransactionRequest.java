package com.haot.point.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "포인트 적립/사용 REQUEST DTO")
public record PointTransactionRequest(
        @Schema(description = "0 이상 입력해야 합니다.")
        @NotNull @Min(0) Double points,
        @Schema(description = "USE, EARN 만 요청 가능합니다.")
        @NotBlank String type,
        @Schema(description = "포인트 상태 변경이 일어난 이유")
        String contextId
) {}
