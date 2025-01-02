package com.haot.coupon.application.dto.request.coupons;

import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;

import java.time.LocalDateTime;

public record CouponSearchRequest(
        String keyword,
        Boolean isDelete,
        LocalDateTime searchStartDate,
        LocalDateTime searchEndDate,
        CouponType couponType,
        DiscountPolicy discountPolicy,
        Double minAmount,
        Double maxAmount,
        Integer discountRate,
        Double discountAmount
) {
}
