package com.haot.coupon.application.dto.request;

import java.time.LocalDateTime;

public record EventCreateRequest(

        String couponId,
        LocalDateTime eventStartDate,
        LocalDateTime eventEndDate,
        String eventName,
        String eventDescription
) {
}
