package com.haot.coupon.application.dto.response.events;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "이벤트 생성 RESPONSE DTO")
@Builder
public record EventCreateResponse(
        String eventId,
        String couponId,
        String eventStatus
) {
}
