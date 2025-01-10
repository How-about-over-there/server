package com.haot.coupon.application.dto.response.coupons;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CouponReadMeResponse(
        String couponId,
        String couponName,
        LocalDateTime couponAvailableDate,
        LocalDateTime couponExpiredDate,
        double minAvailableAmount, // 최소 사용 금액
        double maxAvailableAMount, // 쿠폰 최대 사용할 수 있는 금액
        Integer discountRate,   // 할인율
        Double discountAmount   // 할인 금액
) {
}
