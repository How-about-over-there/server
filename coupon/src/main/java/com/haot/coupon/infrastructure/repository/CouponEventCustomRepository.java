package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.application.dto.request.events.EventSearchRequest;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponEventCustomRepository {
    Page<EventSearchResponse> searchEventByRole(EventSearchRequest request, Pageable pageable);
}
