package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;
import jakarta.validation.Valid;

public interface AdminEventService {
    EventCreateResponse create(EventCreateRequest eventCreateRequest);
}
