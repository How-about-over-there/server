package com.haot.coupon.application.dto.request.coupons;

import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;

import java.time.LocalDateTime;

public record CouponCreateRequest(
        String couponName,
        LocalDateTime couponAvailableDate,
        LocalDateTime couponExpiredDate,
        CouponType couponType,
        DiscountPolicy discountPolicy,
        Integer minimumAmount,
        Integer maximumAmount,
        Integer discountRate,
        Integer discountAmount,
        Integer maxQuantity,
        Integer issuedQuantity
) {
}
