package com.haot.review.application.dtos.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "리뷰 업데이트 요청 정보")
public record ReviewUpdateRequest(
    @Schema(description = "리뷰 수정 내용", example = "쾌적한 숙소입니다.")
    @NotBlank String contents
) {
}
