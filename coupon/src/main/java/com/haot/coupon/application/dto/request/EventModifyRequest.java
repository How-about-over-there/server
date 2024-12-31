package com.haot.coupon.application.dto.request;

import com.haot.coupon.domain.model.enums.EventStatus;

public record EventModifyRequest(
        EventStatus eventStatus
) {
}
