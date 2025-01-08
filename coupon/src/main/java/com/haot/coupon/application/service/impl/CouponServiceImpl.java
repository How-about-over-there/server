package com.haot.coupon.application.service.impl;

import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.kafka.CouponErrorProducer;
import com.haot.coupon.application.mapper.UserCouponMapper;
import com.haot.coupon.application.service.CouponService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.EventStatus;
import com.haot.coupon.infrastructure.repository.CouponEventRepository;
import com.haot.coupon.infrastructure.repository.CouponRepository;
import com.haot.coupon.infrastructure.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponEventRepository couponEventRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserCouponMapper userCouponMapper;
    private final CouponErrorProducer couponErrorProducer;

    @Transactional
    @Override
    public void customerIssueCoupon(CouponCustomerCreateRequest request, String userId) {

        // 공통일때

        // 이벤트 상태값이 DEFAULT 이벤트 id로 있는지 db에 체크
        CouponEvent event = couponEventRepository.findByIdAndEventStatusAndIsDeleteFalse(request.eventId(), EventStatus.DEFAULT)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));

        // userCoupon테이블에 userId와 couponId가 같은 데이터 있는지 체크 -> 있으면 exception
        checkAlreadyIssued(userId, request.couponId());

        // Couponid로 쿠폰 있는지 db 체크
        Coupon coupon = checkExistsCoupon(request.couponId());

        checkEventDate(event, LocalDateTime.now());


        // TODO 무제한일때 kafka로 부하분산하고 redis로 비동기 처리? 트러블 슈팅

        // TODO 선착순일때 순서보장되야 된다. kafka나 Queue, redis등 무었을 쓸지 고민하여 넣어야 한다.
        // TODO redis에 쿠폰 수량 저장해놔 동시성 문제 해결하기
        if(coupon.getType() == CouponType.PRIORITY){
            checkPriorityCouponStock(event, coupon);
        }

        userCouponRepository.save(userCouponMapper.toEntity(userId, coupon));

        couponRepository.increaseIssuedQuantity(coupon.getId());

    }

    @Transactional
    public void updateEndEventStatus(String eventId, EventStatus newStatus) {
        CouponEvent event = couponEventRepository.findById(eventId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));
        event.updateEventStatus(newStatus);
        couponEventRepository.save(event);
    }


    // 이미 발급된 쿠폰인지 check
    private void checkAlreadyIssued(String userId, String couponId) {

        if(userCouponRepository.existsByUserIdAndCouponId(userId, couponId)){
            throw new CustomCouponException(ErrorCode.DUPLICATED_ISSUED_COUPON);
        }

    }

    // 쿠폰 DB check
    private Coupon checkExistsCoupon(String couponId) {

        return couponRepository.findByIdAndIsDeleteFalse(couponId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.COUPON_NOT_FOUND));
    }

    // event 시작, 끝 날짜 체크
    private void checkEventDate(CouponEvent event, LocalDateTime now) {

        if(now.isBefore(event.getEventStartDate())){
            throw new CustomCouponException(ErrorCode.CURRENT_EVENT_NOT_STARTED);
        }

        if(!now.isBefore(event.getEventEndDate())){
                // kafka send 명확해서 after commit이 없어도된다.
            couponErrorProducer.sendEventExpired(EventStatus.EXPIRED + " " + event.getId());
            throw new CustomCouponException(ErrorCode.CURRENT_EVENT_EXPIRED);
        }

    }

    private void checkPriorityCouponStock(CouponEvent event, Coupon coupon) {

        if(coupon.getTotalQuantity() <= coupon.getIssuedQuantity()){
            couponErrorProducer.sendEventOutOfStock(EventStatus.OUT_OF_STOCK + " " + event.getId());
            throw new CustomCouponException(ErrorCode.CURRENT_EVENT_END_TO_OUT_OF_STOCK);
        }

    }



}
