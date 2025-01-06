package com.haot.coupon.application.dto.request.coupons;

import com.haot.coupon.domain.model.enums.CouponType;
import com.haot.coupon.domain.model.enums.DiscountPolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CouponCreateRequest(
        @NotBlank
        String couponName,
        LocalDateTime couponAvailableDate,
        LocalDateTime couponExpiredDate,
        CouponType couponType,
        DiscountPolicy discountPolicy,
        @NotNull
        Double minimumAmount,
        @NotNull
        Double maximumAmount,
        Integer discountRate,
        Double discountAmount,
        Integer maxQuantity,
        Integer issuedQuantity
) {
}
