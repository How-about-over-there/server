package com.haot.coupon.application.dto.response.coupons;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "쿠폰 생성 RESPONSE DTO")
@Builder
public record CouponCreateResponse(
        String couponId
) {
}
