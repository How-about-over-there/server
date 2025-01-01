package com.haot.coupon.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

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
