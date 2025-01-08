package com.haot.coupon.application.service.impl;

import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.application.mapper.EventMapper;
import com.haot.coupon.application.service.EventService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.infrastructure.repository.CouponEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final CouponEventRepository couponEventRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    @Override
    public EventSearchResponse getEvent(String eventId) {

        CouponEvent couponEvent = couponEventRepository.findById(eventId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));

        return eventMapper.toSearchResponse(couponEvent);
    }
}
