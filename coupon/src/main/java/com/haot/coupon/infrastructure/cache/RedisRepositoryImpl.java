package com.haot.coupon.infrastructure.cache;

import com.haot.coupon.application.cache.RedisRepository;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, Integer> countRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final String EVENT_PREFIX = "event:";
    private final String COUPON_PREFIX = "c:";
    private final String USER_PREFIX = "user:";


    // event 종료 날짜로 save
    @Override
    public void save(CouponEvent savedEvent, Coupon coupon) {

        Duration expired = calculateTTL(savedEvent.getEventEndDate());

        countRedisTemplate.opsForValue()
                .set(EVENT_PREFIX + savedEvent.getId() + COUPON_PREFIX + coupon.getId(),
                        coupon.getTotalQuantity(), expired);

    }

    // user가 이미 쿠폰을 발급 받았는지 확인
    @Override
    public boolean existsIssuedCouponByUserId(String userId, String couponId) {
        String key = COUPON_PREFIX + couponId;
        String value = USER_PREFIX + userId;

        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    @Override
    public Long decreaseCouponQuantity(String eventId, String couponId) {
        return countRedisTemplate.opsForValue()
                .decrement(EVENT_PREFIX + eventId + COUPON_PREFIX + couponId);
    }

    @Override
    public void increaseCouponQuantity(String eventId, String couponId) {
        countRedisTemplate.opsForValue().increment(EVENT_PREFIX + eventId + COUPON_PREFIX + couponId);
    }

    @Override
    public void issueCoupon(String userId, String couponId, LocalDateTime eventEndDate) {
        String key = COUPON_PREFIX + couponId;
        String value = USER_PREFIX + userId;
        Duration expired = calculateTTL(eventEndDate);

        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForSet().add(key, value);
            redisTemplate.expire(key, expired);  // 만료 시간 설정
        } else {
            redisTemplate.opsForSet().add(key, value);  // 이미 Set이 있으면 값만 추가
        }
    }

    @Override
    public Integer getCouponQuantityByIds(String eventId, String couponId) {
        return countRedisTemplate.opsForValue().get(EVENT_PREFIX + eventId + COUPON_PREFIX + couponId);
    }

    // expired 시간 계산
    private Duration calculateTTL(LocalDateTime expirationDate) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, expirationDate);
    }






}
