package com.haot.coupon.application.dto.feign.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "[Feign] 쿠폰 유효성 검사 REQUEST DTO")
public record FeignVerifyRequest(
        String couponId,
        double reservationPrice
) {
}
