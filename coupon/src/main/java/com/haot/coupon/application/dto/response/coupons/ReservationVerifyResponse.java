package com.haot.coupon.application.dto.response.coupons;

import lombok.Builder;

@Builder
public record ReservationVerifyResponse(

        String reservationCouponId,
        Double discountedPrice
) {
}
