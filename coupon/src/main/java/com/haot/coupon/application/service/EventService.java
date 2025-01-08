package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.response.events.EventSearchResponse;

public interface EventService {
    EventSearchResponse getEvent(String eventId);
}
