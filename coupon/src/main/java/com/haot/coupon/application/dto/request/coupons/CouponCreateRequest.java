package com.haot.coupon.application.dto.request.coupons;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record CouponCreateRequest(
        @NotBlank
        String couponName,

        @FutureOrPresent
        @NotNull
        LocalDateTime couponAvailableDate,

        @FutureOrPresent
        @NotNull
        LocalDateTime couponExpiredDate,

        @Pattern(regexp = "UNLIMITED|PRIORITY", message = "유효한 상태 값을 입력하세요.")
        @NotNull
        String couponType,

        @Pattern(regexp = "PERCENTAGE|AMOUNT", message = "유효한 상태 값을 입력하세요.")
        @NotNull
        String discountPolicy,

        @NotNull
        Double minDiscountAmount, // 쿠폰 사용시 최소 사용 금액

        @NotNull
        Double maxDiscountAmount, // 쿠폰 사용 할 수 있는 최대 금액

        Integer discountRate,

        Double discountAmount,

        @NotNull
        Integer totalQuantity,

        int issuedQuantity
) {
}
