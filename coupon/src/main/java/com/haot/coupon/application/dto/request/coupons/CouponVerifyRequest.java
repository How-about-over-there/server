package com.haot.coupon.application.dto.request.coupons;

public record CouponVerifyRequest(
        String userId,
        Integer reservationPrice
) {
}
