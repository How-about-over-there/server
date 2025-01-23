package com.haot.coupon.application.dto.request.events;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "관리자 이벤트 수정 검사 REQUEST DTO")
public record EventModifyRequest(

        String eventName,

        String eventDescription,

        @Schema(description = "MANUALLY_CLOSED 만 요청 가능합니다.")
        @Pattern(regexp = "MANUALLY_CLOSED", message = "유효한 상태 값을 입력하세요.")
        String eventStatus
) {
}
