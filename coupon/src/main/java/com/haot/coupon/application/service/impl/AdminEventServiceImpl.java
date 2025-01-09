package com.haot.coupon.application.service.impl;

import com.haot.coupon.application.dto.request.events.EventCreateRequest;
import com.haot.coupon.application.dto.request.events.EventModifyRequest;
import com.haot.coupon.application.dto.response.events.EventCreateResponse;
import com.haot.coupon.application.kafka.CouponErrorProducer;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final CouponEventRepository couponEventRepository;
    private final CouponRepository couponRepository;

    private final EventMapper mapper;

    private final CouponErrorProducer couponErrorProducer;

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
                        if (!(couponEvent.getEventStatus() == EventStatus.EXPIRED || couponEvent.getEventStatus() == EventStatus.MANUALLY_CLOSED)) {
                            throw new CustomCouponException(ErrorCode.EXIST_UNLIMITED_COUPON_EVENTS);
                        }
                    })
                    .toList();

        }

        // mapper로 event 만든다.
        CouponEvent event = mapper.toEntity(eventCreateRequest, coupon);

        // 현재 시간으로 eventStatus update
        event.updateExpiredEventStatus();

        return mapper.toCreateResponse(couponEventRepository.save(event));
    }

    // 이벤트 수정 API
    @Transactional
    @Override
    public void modify(String eventId, EventModifyRequest request) {

        validateNonEmptyFields(request.eventName(), request.eventDescription(), request.eventStatus());

        CouponEvent event = couponEventRepository.findByIdAndIsDeleteFalse(eventId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));

        // 예시) event description -> 쿠폰에서 문제가 있어 이벤트 강제 종료.. 로 변경?
        event.modifyEvent(request.eventName(), request.eventDescription());

        // status 수정시 이벤트 관리자 강제 종료 -> 거의 이 용도로 쓰게 끔
        if(request.eventStatus() != null){
            couponErrorProducer.sendEventClosed(EventStatus.MANUALLY_CLOSED + " " + event.getId());
        }
    }

    // 유효성 검사 함수: 필드들이 모두 비어 있을 경우 예외 던짐
    private void validateNonEmptyFields(String eventName, String eventDescription, String eventStatus) {
        boolean allFieldsEmpty = Stream.of(eventName, eventDescription, eventStatus)
                .allMatch(field -> field == null || field.isEmpty());

        if (allFieldsEmpty) {
            throw new CustomCouponException(ErrorCode.MODIFY_EVENT_HAS_NO_PARAMETER);
        }
    }
}
