package com.haot.coupon.infrastructure.cache;

import com.haot.coupon.application.cache.RedisRepository;
import com.haot.coupon.application.dto.CheckAlreadyClosedEventDto;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.utils.CouponIssueRedisCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j(topic = "RedisRepositoryImpl")
@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, Integer> countRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    private final RedisScript<String> limitedCouponIssueScript;
    private final RedisScript<String> unlimitedCouponIssueScript;
    private final String EVENT_PREFIX = "event:";
    private final String COUPON_PREFIX = "c:";
    private final String USER_PREFIX = "user:";


    // event 종료 날짜로 save
    @Override
    public void save(CouponEvent savedEvent, Coupon coupon) {

        Duration expired = calculateTTL(savedEvent.getEventEndDate());

        countRedisTemplate.opsForValue()
                .set(getPriorityCouponKey(savedEvent.getId(), coupon.getId()),
                        coupon.getTotalQuantity(), expired);

    }

    @Override
    public CouponIssueRedisCode issuePriorityCoupon(String eventId, String couponId, String userId, LocalDateTime eventEndDate) {

        String countKey = getPriorityCouponKey(eventId, couponId);
        String duplicatedCheckKey = getDuplicatedUserKey(couponId);

        String result = redisTemplate.execute(
                limitedCouponIssueScript,
                List.of(countKey, duplicatedCheckKey),
                userId,
                String.valueOf(calculateTTL(eventEndDate).getSeconds())
        );

        return CouponIssueRedisCode.checkCouponIssueRedisCode(result);
    }

    @Override
    public CouponIssueRedisCode issueUnlimitedCoupon(String couponId, String userId, LocalDateTime eventEndDate) {

        String duplicatedCheckKey = getDuplicatedUserKey(couponId);

        String result = redisTemplate.execute(
                unlimitedCouponIssueScript,
                List.of(duplicatedCheckKey),
                userId,
                String.valueOf(calculateTTL(eventEndDate).getSeconds())
        );
        return CouponIssueRedisCode.checkCouponIssueRedisCode(result);
    }

    // todo 삭제해야 된다.
    @Override
    public void increaseCouponQuantity(String eventId, String couponId) {
        countRedisTemplate.opsForValue().increment(EVENT_PREFIX + eventId + COUPON_PREFIX + couponId);
    }


    //  todo 삭제해야 된다. -> 이것도 필요 없을듯 LUASCRIPT가 한번에 해주면?
    @Override
    public Integer getCouponQuantityByIds(String eventId, String couponId) {
        return countRedisTemplate.opsForValue().get(EVENT_PREFIX + eventId + COUPON_PREFIX + couponId);
    }

    // TODO 동시에 많은 쓰레드 요청시 삭제 후 다시 생기는 일 발생
    @Override
    public void deleteEventClosed(List<CheckAlreadyClosedEventDto> couponEvents) {

        couponEvents.forEach(couponEvent -> {

            String eventCouponKey = EVENT_PREFIX + couponEvent.getEventId() + COUPON_PREFIX + couponEvent.getCouponId();
            String couponKey = COUPON_PREFIX + couponEvent.getCouponId();

            Boolean isEventCouponDeleted = redisTemplate.delete(eventCouponKey);
            Boolean isCouponDeleted = redisTemplate.delete(couponKey);

            // 모니터링을 위한 로그
            if (Boolean.TRUE.equals(isEventCouponDeleted)) {
                log.info("Successfully deleted event-coupon key: {}", eventCouponKey);
            } else {
                log.warn("Failed to delete event-coupon key (not found): {}", eventCouponKey);
            }

            if (Boolean.TRUE.equals(isCouponDeleted)) {
                log.info("Successfully deleted coupon key: {}", couponKey);
            } else {
                log.warn("Failed to delete coupon key (not found): {}", couponKey);
            }

        });


    }

    private String getDuplicatedUserKey(String couponId) {
        return COUPON_PREFIX + couponId;
    }

    // 선착순 쿠폰을 가진 이벤트 key
    private String getPriorityCouponKey(String eventId, String couponId) {
        return EVENT_PREFIX + eventId + COUPON_PREFIX + couponId;
    }

    // expired 시간 계산
    private Duration calculateTTL(LocalDateTime expirationDate) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, expirationDate);
    }






}
