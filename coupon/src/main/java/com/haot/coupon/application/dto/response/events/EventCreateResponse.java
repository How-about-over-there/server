package com.haot.coupon.application.dto.response.events;

import lombok.Builder;

@Builder
public record EventCreateResponse(
        String eventId,
        String couponId,
        String eventStatus
) {
}
