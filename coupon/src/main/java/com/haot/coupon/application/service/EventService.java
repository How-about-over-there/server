package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.request.events.EventSearchRequest;
import com.haot.coupon.application.dto.response.PageResponse;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Pageable;

public interface EventService {
    EventSearchResponse getEvent(String eventId);

    PageResponse<EventSearchResponse> searchEvent(Role userRole, EventSearchRequest request, Pageable pageable);
}
