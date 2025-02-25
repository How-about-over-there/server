package com.haot.coupon.application.cache;

import com.haot.coupon.application.dto.CheckAlreadyClosedEventDto;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.utils.CouponIssueRedisCode;

import java.time.LocalDateTime;
import java.util.List;

public interface RedisRepository {
    void save(CouponEvent savedEvent, Coupon coupon);

    CouponIssueRedisCode issuePriorityCoupon(String eventId, String couponId, String userId, LocalDateTime eventEndDate);

    void deleteEventClosed(List<CheckAlreadyClosedEventDto> list);

    CouponIssueRedisCode issueUnlimitedCoupon(String couponId, String userId, LocalDateTime eventEndDate);
}
