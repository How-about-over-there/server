package com.haot.coupon.application.service.impl;

import com.haot.coupon.application.cache.RedisRepository;
import com.haot.coupon.application.dto.feign.request.FeignConfirmReservationRequest;
import com.haot.coupon.application.dto.feign.request.FeignVerifyRequest;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.dto.feign.response.ReservationVerifyResponse;
import com.haot.coupon.application.kafka.CouponErrorProducer;
import com.haot.coupon.application.kafka.CouponIssueProducer;
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
import com.haot.coupon.infrastructure.repository.*;
import com.haot.submodule.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponEventRepository couponEventRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final ReservationCouponRepository reservationCouponRepository;

    private final RedisRepository redisRepository;

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    private final ReservationCouponMapper reservationCouponMapper;

    private final CouponErrorProducer couponErrorProducer;
    private final CouponIssueProducer couponIssueProducer;

    // 쿠폰 발급 API
    @Transactional
    @Override
    public void customerIssueCoupon(CouponCustomerCreateRequest request, String userId) {

        // 공통일때

        // 이벤트 상태값이 DEFAULT 이벤트 id로 있는지 db에 체크, coupon도 같이 불러온다.
        CouponEvent event = couponEventRepository.findByIdAndEventStatusAndIsDeleteFalse(request.eventId(), EventStatus.DEFAULT)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));

        Coupon coupon = event.getCoupon();

        if(!coupon.getId().equals(request.couponId())){
            throw new CustomCouponException(ErrorCode.COUPON_NOT_MATCHED_WITH_EVENT);
        }

        // userCoupon테이블에 userId와 couponId가 같은 데이터 있는지 체크 -> 있으면 exception
        checkAlreadyIssued(userId, request.couponId());

        // 이벤트 시작 날짜 전인지 체크, 후면 expired로 kafka send
        checkEventDate(event, LocalDateTime.now());

        if(coupon.getType() == CouponType.PRIORITY){
            checkPriorityCouponStock(event.getId(), coupon.getId(), userId, event.getEventEndDate());
            couponIssueProducer.sendIssuePriorityCoupon(userId, request); // TODO auditoraware 사용해 updateby에 잘들어가게 해야된다.

            // TODO 일단 무제한은 개발 하지 않았고 선착순 2번 insert 안되게 막아놨다.
        }else{
            userCouponRepository.save(userCouponMapper.toEntity(userId, coupon));

            couponRepository.increaseIssuedQuantity(coupon.getId());
        }

        // TODO 무제한일때 따로 kafka로 부하분산하고 redis로 비동기 처리? 트러블 슈팅

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

        // reservationCoupon 테이블 체크
        checkReservedCouponAvailable(userCoupon);

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

    // [Feign] 쿠폰 상태 변경 API
    @Transactional
    @Override
    public void confirmReservation(String reservationCouponId, FeignConfirmReservationRequest request) {
        ReservationCoupon reservationCoupon = reservationCouponRepository.findById(reservationCouponId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.RESERVATION_COUPON_NOT_FOUND));

        // 선점 상태가 아닌 경우 에러 반환
        validateReservationPreemption(reservationCoupon);

        ReservationCouponStatus reservationCouponStatus = ReservationCouponStatus.checkReservationCouponStatus(request.reservationStatus());

        // 예약 상태 처리
        handleReservation(reservationCoupon, reservationCouponStatus);
    }

    // 내 쿠폰함 보기 API
    @Transactional(readOnly = true)
    @Override
    public Page<CouponReadMeResponse> getMyCoupons(String userId, Pageable pageable) {
        return userCouponRepository.checkMyCouponBox(userId, pageable);
    }

    // 쿠폰 Rollback API
    @Transactional
    @Override
    public void rollbackReservationCoupon(String userId, Role role, String reservationCouponId) {

        ReservationCoupon reservationCoupon = reservationCouponRepository.findById(reservationCouponId)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.RESERVATION_COUPON_NOT_FOUND));

        UserCoupon userCoupon = reservationCoupon.getUserCoupon();

        // Role이 User일때 userId 같은지 체크
        validateUserAndRole(userId, role, userCoupon.getUserId());

        // 선점 상태가 아닌 경우 에러 반환
        validateReservationPreemption(reservationCoupon);

        reservationCoupon.confirmReservationStatus(ReservationCouponStatus.ROLLBACK);
    }

    @Transactional
    @Override
    public void issuePriorityCoupon(String userId, CouponCustomerCreateRequest request) {
        Coupon coupon = checkExistsCoupon(request.couponId());

        if(userCouponRepository.existsByUserIdAndCouponIdAndIsDeleteFalse(userId, coupon.getId())){
            throw new CustomCouponException(ErrorCode.DUPLICATED_ISSUED_COUPON);
        }

        userCouponRepository.save(userCouponMapper.toEntity(userId, coupon));

        couponRepository.increaseIssuedQuantity(coupon.getId());

    }

    // 선점 상태 검증
    private void validateReservationPreemption(ReservationCoupon reservationCoupon) {
        if (reservationCoupon.getReservationCouponStatus() != ReservationCouponStatus.PREEMPTION) {
            throw new CustomCouponException(ErrorCode.RESERVATION_COUPON_NOT_PREEMPTED);
        }
    }

    // User & Role 체크
    private void validateUserAndRole(String userId, Role role, String dbUserId) {
        if(role == Role.USER){
            if(!dbUserId.equals(userId)){
                throw new CustomCouponException(ErrorCode.USER_NOT_MATCHED);
            }
        }
    }

    // 예약 상태 처리
    private void handleReservation(ReservationCoupon reservationCoupon, ReservationCouponStatus reservationCouponStatus) {
        switch (reservationCouponStatus) {
            case COMPLETED -> handleReservationCompleted(reservationCoupon);
            case CANCEL -> handleReservationCanceled(reservationCoupon);
            default -> throw new CustomCouponException(ErrorCode.RESERVATION_STATUS_NOT_MATCH);
        }
    }

    // 예약 완료 처리
    private void handleReservationCompleted(ReservationCoupon reservationCoupon) {
        UserCoupon userCoupon = reservationCoupon.getUserCoupon();

        // 쿠폰 상태 및 완료 처리
        userCoupon.reservationComplete();
        reservationCoupon.confirmReservationStatus(ReservationCouponStatus.COMPLETED);
    }

    // 예약 취소 처리
    private void handleReservationCanceled(ReservationCoupon reservationCoupon) {
        UserCoupon userCoupon = reservationCoupon.getUserCoupon();
        Coupon coupon = userCoupon.getCoupon();

        // 만료 시간 기준 상태 결정
        ReservationCouponStatus status = LocalDateTime.now().isAfter(coupon.getExpiredDate()) ?
                ReservationCouponStatus.EXPIRED : ReservationCouponStatus.CANCEL;

        // 쿠폰 상태 및 취소 처리
        userCoupon.reservationCancel();
        reservationCoupon.confirmReservationStatus(status);
    }

    // user 쿠폰이 reservationCoupon 테이블에 CANCEL, ROLLBACK 상태가 아닌 다른 상태값이 DB에 있으면 사용불가
    private void checkReservedCouponAvailable(UserCoupon userCoupon) {

        if (reservationCouponRepository.existsByUserCouponAndReservationCouponStatusNotInAndIsDeleteFalse(
                userCoupon, List.of(ReservationCouponStatus.ROLLBACK, ReservationCouponStatus.CANCEL))) {
            throw new CustomCouponException(ErrorCode.COUPON_UNAVAILABLE);
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


    // 이벤트 상태 변경 consumer, redis 삭제
    @Transactional
    public void updateEndEventStatus(String eventId, EventStatus newStatus) {
        CouponEvent event = couponEventRepository.findByIdAndEventStatusAndIsDeleteFalse(eventId, EventStatus.DEFAULT)
                .orElseThrow(() -> new CustomCouponException(ErrorCode.EVENT_NOT_FOUND));

        redisRepository.deleteEventClosed(event.getId(), event.getCoupon().getId());
        event.updateEventStatus(newStatus);
        couponEventRepository.save(event);
    }

    // 이미 발급된 쿠폰인지 check
    private void checkAlreadyIssued(String userId, String couponId){

        if(redisRepository.existsIssuedCouponByUserId(userId, couponId)){
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
            couponErrorProducer.sendEventClosed(EventStatus.EXPIRED + " " + event.getId());
            throw new CustomCouponException(ErrorCode.CURRENT_EVENT_CLOSED);
        }

    }

    // 발급된 쿠폰수가 최대 발급 수량보다 클때 이벤트 종료
    private void checkPriorityCouponStock(String eventId, String couponId, String userId, LocalDateTime eventEndDate) {

        Long couponQuantity = redisRepository.decreaseCouponQuantity(eventId, couponId);

        if(couponQuantity != null && couponQuantity < 0){
            redisRepository.increaseCouponQuantity(eventId, couponId);
            couponErrorProducer.sendEventClosed(EventStatus.OUT_OF_STOCK + " " + eventId);
            throw new CustomCouponException(ErrorCode.CURRENT_EVENT_END_TO_OUT_OF_STOCK);
        }

        redisRepository.issueCoupon(userId, couponId, eventEndDate);

    }



}
