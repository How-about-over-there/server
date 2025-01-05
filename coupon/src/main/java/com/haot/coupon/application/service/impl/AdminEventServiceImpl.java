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
import com.haot.coupon.domain.model.enums.EventStatus;
import com.haot.coupon.infrastructure.repository.CouponEventRepository;
import com.haot.coupon.infrastructure.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        Coupon coupon = couponRepository.findByIdAndExpiredDateIsAfterAndIsDeleteFalse(eventCreateRequest.couponId(),
                        eventCreateRequest.eventEndDate())
                .orElseThrow(() -> new CustomCouponException(ErrorCode.COUPON_NOT_FOUND));


        // 이벤트에 같은 선착순 쿠폰이 설정되면 안된다, 무제한은 가능하게!
        if(CouponType.PRIORITY == coupon.getType() && couponEventRepository.existsByCouponIdAndIsDeleteFalse(coupon.getId())) {
            throw new CustomCouponException(ErrorCode.EXIST_PRIORITY_COUPON_EVENTS);
        }

        // 무제한은 이벤트들 안에서 여러번 같은 쿠폰을 쓸 수 있게 한다.
        if(CouponType.UNLIMITED == coupon.getType()){

            List<CouponEvent> promotions = couponEventRepository
                    .findByCouponIdAndEventEndDateIsAfterAndIsDeleteFalse(coupon.getId(), LocalDateTime.now())
                    .stream()
                    .peek(couponEvent -> {
                        if (couponEvent.getEventStatus() != EventStatus.END) {
                            throw new CustomCouponException(ErrorCode.EXIST_UNLIMITED_COUPON_EVENTS);
                        }
                    })
                    .toList();

        }

        // mapper로 event 만든다.
        CouponEvent event = mapper.toEntity(eventCreateRequest, coupon);

        // 현재 시간으로 eventStatus update
        event.updateEventStatus();

        return mapper.toCreateResponse(couponEventRepository.save(event));
    }
}
