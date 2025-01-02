package com.haot.coupon.application.dto.request.events;

import java.time.LocalDateTime;

public record EventSearchRequest(

        String keyword,
        Boolean isDelete,
        LocalDateTime startDate,
        LocalDateTime endDate

) {
}
