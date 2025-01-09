package com.haot.coupon.application.service.impl;

import com.haot.coupon.application.dto.feign.request.FeignVerifyRequest;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.dto.response.coupons.ReservationVerifyResponse;
import com.haot.coupon.application.kafka.CouponErrorProducer;
import com.haot.coupon.application.mapper.CouponMapper;
import com.haot.coupon.application.mapper.ReservationCouponMapper;
import com.haot.coupon.application.mapper.UserCouponMapper;
import com.haot.coupon.application.service.CouponService;
import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.model.ReservationCoupon;
import com.haot.coupon.domain.model.UserCoupon;
import com.haot.coupon.domain.model.enums.*;
import com.haot.coupon.infrastructure.repository.CouponEventRepository;
import com.haot.coupon.infrastructure.repository.CouponRepository;
import com.haot.coupon.infrastructure.repository.ReservationCouponRepository;
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
    private final ReservationCouponRepository reservationCouponRepository;

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    private final ReservationCouponMapper reservationCouponMapper;

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

    // 쿠폰 단건 조회 API
    @Transactional(readOnly = true)
    @Override
    public CouponSearchResponse getCouponDetails(String couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.COUPON_NOT_FOUND));

        return couponMapper.toSearchResponse(coupon);
    }

    // 쿠폰 유효성 검사 API
    @Transactional
    @Override
    public ReservationVerifyResponse verify(FeignVerifyRequest request) {

        Coupon coupon = checkExistsCoupon(request.couponId());

        UserCoupon userCoupon = findUserCoupon(request.userId(), coupon);

        // 선점 체크
        checkAlreadyReserved(userCoupon);

        // 쿠폰 상태가 USED인지 확인하고 에러 반환
        checkIfCouponUsed(userCoupon);

        // 금액, 날짜 체크
        validateBeforeReservation(coupon, request);

        //할인 정책에 따라 금액 계산
        double totalPrice = request.reservationPrice();
        double discountPrice = getDiscountedPrice(coupon, totalPrice);

        ReservationCoupon reservationCoupon = reservationCouponRepository.save(
                reservationCouponMapper.toEntity(userCoupon, totalPrice, discountPrice)
        );

        return reservationCouponMapper.toVerifyFeignResponse(reservationCoupon.getId(), totalPrice - discountPrice);
    }

    // 쿠폰 선점 상태인지 확인
    private void checkAlreadyReserved(UserCoupon userCoupon) {

        if(reservationCouponRepository.existsByUserCouponAndReservationCouponStatusAndIsDeleteFalse(
                userCoupon, ReservationCouponStatus.PREEMPTION)
        ){
            throw new CustomCouponException(ErrorCode.COUPON_ALREADY_RESERVED);
        }
    }

    // UserCoupon 조회 메소드
    private UserCoupon findUserCoupon(String userId, Coupon coupon) {
        return userCouponRepository.findByUserIdAndCouponIdAndIsDeleteFalse(userId, coupon.getId())
                .orElseThrow(() -> new CustomCouponException(ErrorCode.USER_COUPON_NOT_FOUND));
    }

    // 쿠폰 상태가 USED인지 확인
    private void checkIfCouponUsed(UserCoupon userCoupon) {
        if (userCoupon.getCouponStatus() == CouponStatus.USED) {
            throw new CustomCouponException(ErrorCode.COUPON_ALREADY_USED);
        }
    }

    // 할인 금액 연산
    private double getDiscountedPrice(Coupon coupon, double totalPrice) {
        if (coupon.getDiscountPolicy() == DiscountPolicy.PERCENTAGE) {
            return calculatePercentageDiscount(coupon, totalPrice);
        } else {
            return calculateFixedDiscount(coupon);
        }
    }

    // 퍼센트 할인 계산
    private double calculatePercentageDiscount(Coupon coupon, double totalPrice) {
        if (coupon.getDiscountRate() == null) {
            throw new CustomCouponException(ErrorCode.DISCOUNT_POLICY_NOT_MATCH);
        }
        return (totalPrice * coupon.getDiscountRate().getRate()) / 100.0;
    }

    // 고정 할인 금액 계산
    private double calculateFixedDiscount(Coupon coupon) {
        if (coupon.getDiscountAmount() == null) {
            throw new CustomCouponException(ErrorCode.DISCOUNT_POLICY_NOT_MATCH);
        }
        return coupon.getDiscountAmount();
    }

    private void validateBeforeReservation(Coupon coupon, FeignVerifyRequest request) {
        validateCouponDate(coupon.getAvailableDate(), coupon.getExpiredDate(), LocalDateTime.now());
        validateCouponAmount(coupon, request.reservationPrice());
    }

    // 쿠폰 금액 체크
    private void validateCouponAmount(Coupon coupon, double reservationPrice) {
        if (coupon.getMinAvailableAmount() > reservationPrice || coupon.getMaxAvailableAmount() < reservationPrice) {
            throw new CustomCouponException(ErrorCode.INVALID_PAYMENT_AMOUNT_FOR_COUPON);
        }
    }

    // 현재시간 쿠폰 시간 유효성 검사
    private void validateCouponDate(LocalDateTime availableDate, LocalDateTime expiredDate, LocalDateTime now) {
        if (now.isBefore(availableDate) || now.isAfter(expiredDate)) {
            throw new CustomCouponException(ErrorCode.COUPON_USED_WRONG_TIME);
        }
    }


    // 이벤트 상태 변경 API
    @Transactional
    public void updateEndEventStatus(String eventId, EventStatus newStatus) {
        CouponEvent event = couponEventRepository.findById(eventId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));
        event.updateEventStatus(newStatus);
        couponEventRepository.save(event);
    }


    // 이미 발급된 쿠폰인지 check
    private void checkAlreadyIssued(String userId, String couponId) {

        if(userCouponRepository.existsByUserIdAndCouponIdAndIsDeleteFalse(userId, couponId)){
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
