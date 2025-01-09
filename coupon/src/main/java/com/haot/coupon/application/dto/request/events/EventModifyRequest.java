package com.haot.coupon.application.dto.request.events;

import jakarta.validation.constraints.Pattern;

public record EventModifyRequest(

        String eventName,

        String eventDescription,

        @Pattern(regexp = "MANUALLY_CLOSED", message = "유효한 상태 값을 입력하세요.")
        String eventStatus
) {
}
