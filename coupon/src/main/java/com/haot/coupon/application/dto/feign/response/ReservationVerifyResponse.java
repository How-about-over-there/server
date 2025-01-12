package com.haot.coupon.application.dto.feign.response;

import lombok.Builder;

@Builder
public record ReservationVerifyResponse(

        String reservationCouponId,
        Double discountedPrice
) {
}
