package com.haot.coupon.application.service.impl;

import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;
import com.haot.coupon.application.mapper.EventMapper;
import com.haot.coupon.application.service.AdminEventService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.infrastructure.repository.CouponEventRepository;
import com.haot.coupon.infrastructure.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final CouponEventRepository couponEventRepository;
    private final CouponRepository couponRepository;
    private final EventMapper mapper;

    @Transactional
    @Override
    public EventCreateResponse create(EventCreateRequest eventCreateRequest) {

        // Business Logic

        // eventEndDate이 eventStartDate보다 +1일 보다 후여야 한다.
        if (!eventCreateRequest.eventStartDate().plusDays(1).isBefore(eventCreateRequest.eventEndDate())) {
            throw new CustomCouponException(ErrorCode.INSUFFICIENT_DATE_DIFFERENCE);
        }

        // event 끝 날짜가 쿠폰 만료 날짜보다 전이어야 된다.
        // TODO isDelete가 생기면 query에 추가해야 된다. 삭제가 되지 않은 쿠폰 db 체크
        Coupon coupon = couponRepository.findByIdAndExpiredDateIsAfter(eventCreateRequest.couponId(),
                        eventCreateRequest.eventEndDate())
                .orElseThrow(() -> new CustomCouponException(ErrorCode.COUPON_NOT_FOUND));


        // 이벤트에 같은 선착순 쿠폰이 설정되면 안된다, 무제한은 가능하게!
        // TODO isDelete가 생기면 query에 추가해야 된다. 무제한 정책 어떻게 할지..?
        if(CouponType.PRIORITY == coupon.getType() && couponEventRepository.existsByCouponId(coupon.getId())) {
            throw new CustomCouponException(ErrorCode.EXIST_PRIORITY_COUPON_EVENTS);
        }

        // mapper로 event 만든다.
        CouponEvent event = mapper.toEntity(eventCreateRequest, coupon);

        // 현재 시간으로 eventStatus update
        event.updateEventStatus();

        return mapper.toCreateResponse(couponEventRepository.save(event));
    }
}
