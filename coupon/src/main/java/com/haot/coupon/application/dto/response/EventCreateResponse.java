package com.haot.coupon.application.dto.response;

import lombok.Builder;

@Builder
public record EventCreateResponse(
        String eventId
) {
}
