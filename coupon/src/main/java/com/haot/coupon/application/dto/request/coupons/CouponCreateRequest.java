package com.haot.coupon.application.dto.request.coupons;

import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CouponCreateRequest(
        @NotBlank
        String couponName,
        LocalDateTime couponAvailableDate,
        LocalDateTime couponExpiredDate,
        CouponType couponType,
        DiscountPolicy discountPolicy,
        Double minimumAmount,
        Double maximumAmount,
        Integer discountRate,
        Double discountAmount,
        Integer maxQuantity,
        Integer issuedQuantity
) {
}
