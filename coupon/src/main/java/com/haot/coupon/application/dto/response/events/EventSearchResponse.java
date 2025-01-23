package com.haot.coupon.application.dto.response.events;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "이벤트 검색 RESPONSE DTO")
@Builder
public record EventSearchResponse(
        String eventId,
        String couponId,
        LocalDateTime eventStartDate,
        LocalDateTime eventEndDate,
        String eventName,
        String eventDescription
) {
}
