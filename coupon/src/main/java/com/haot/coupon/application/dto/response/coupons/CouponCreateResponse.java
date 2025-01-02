package com.haot.coupon.application.dto.response.coupons;

import lombok.Builder;

@Builder
public record CouponCreateResponse(
        String couponId
) {
}
