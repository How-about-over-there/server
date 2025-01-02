package com.haot.coupon.application.dto.response.coupons;

import com.haot.coupon.domain.model.enums.ReservationCouponStatus;
import lombok.Builder;

@Builder
public record CouponHistoryResponse(

        String reservationCouponId,
        String userCouponId,
        ReservationCouponStatus reservationCouponStatus,
        Integer reservationPrice,
        Integer reservationDiscountedPrice
) {
}
