package com.haot.coupon.application.cache;

import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;

import java.time.LocalDateTime;

public interface RedisRepository {
    void save(CouponEvent savedEvent, Coupon coupon);

    boolean existsIssuedCouponByUserId(String userId, String couponId);

    Long decreaseCouponQuantity(String eventId, String couponId);

    void increaseCouponQuantity(String eventId, String couponId);

    void issueCoupon(String userId, String couponId, LocalDateTime eventEndDate);

    Integer getCouponQuantityByIds(String eventId, String couponId);

    void deleteEventClosed(String eventId, String couponId);
}
