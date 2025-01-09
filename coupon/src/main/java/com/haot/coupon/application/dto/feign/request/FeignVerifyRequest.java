package com.haot.coupon.application.dto.feign.request;

public record FeignVerifyRequest(
        String couponId,
        String userId,
        double reservationPrice
) {
}
