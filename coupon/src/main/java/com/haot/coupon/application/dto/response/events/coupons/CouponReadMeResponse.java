package com.haot.coupon.application.dto.response.events.coupons;

import com.haot.coupon.domain.model.enums.CouponStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CouponReadMeResponse(

        String userCouponId,
        String couponName,
        LocalDateTime couponAvailableDate,
        LocalDateTime couponExpiredDate,
        CouponStatus couponStatus
) {
}
