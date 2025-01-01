package com.haot.coupon.application.dto.response.coupons;

import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CouponSearchResponse(
        String couponId,
        String couponName,
        LocalDateTime couponAvailableDate,
        LocalDateTime couponExpiredDate,
        CouponType couponType,
        DiscountPolicy discountPolicy,
        Integer minimumAmount,
        Integer maximumAmount,
        Integer discountRate,
        Integer discountAmount
) {
}
