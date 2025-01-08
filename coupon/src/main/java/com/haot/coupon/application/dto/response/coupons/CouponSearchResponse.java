package com.haot.coupon.application.dto.response.coupons;

import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CouponSearchResponse(
        String couponId,
        String couponName,
        LocalDateTime availableDate,
        LocalDateTime expiredDate,
        String couponType,
        String discountPolicy,
        Double minAvailableAmount,
        Double maxAvailableAmount,
        Integer discountRate,
        Double discountAmount,
        Integer totalQuantity,
        Integer issuedQuantity
) {
}
