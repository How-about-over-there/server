package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.request.events.EventModifyRequest;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;

public interface AdminEventService {

    EventCreateResponse create(EventCreateRequest eventCreateRequest);

    void modify(String eventId, EventModifyRequest eventModifyRequest);
}
