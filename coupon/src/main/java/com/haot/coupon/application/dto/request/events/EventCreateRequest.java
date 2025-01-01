package com.haot.coupon.application.dto.request.events;

import java.time.LocalDateTime;

public record EventCreateRequest(

        String couponId,
        LocalDateTime eventStartDate,
        LocalDateTime eventEndDate,
        String eventName,
        String eventDescription
) {
}
