package com.haot.coupon.application.dto.feign.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "[Feign] 쿠폰 유효성 검사 RESPONSE DTO")
@Builder
public record ReservationVerifyResponse(

        String reservationCouponId,
        Double discountedPrice
) {
}
