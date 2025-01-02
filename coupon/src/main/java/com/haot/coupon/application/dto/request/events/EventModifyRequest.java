package com.haot.coupon.application.dto.request.events;

import com.haot.coupon.domain.model.enums.EventStatus;

public record EventModifyRequest(
        EventStatus eventStatus
) {
}
