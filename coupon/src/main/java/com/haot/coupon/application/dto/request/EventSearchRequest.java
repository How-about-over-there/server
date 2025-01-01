package com.haot.coupon.application.dto.request;

import java.time.LocalDateTime;

public record EventSearchRequest(

        String keyword,
        Boolean isDelete,
        LocalDateTime startDate,
        LocalDateTime endDate

) {
}
