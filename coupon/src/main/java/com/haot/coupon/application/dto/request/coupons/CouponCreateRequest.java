package com.haot.coupon.application.dto.request.coupons;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Schema(description = "관리자 쿠폰 생성 REQUEST DTO")
public record CouponCreateRequest(
        @NotBlank
        String couponName,

        @FutureOrPresent
        @NotNull
        LocalDateTime couponAvailableDate,

        @FutureOrPresent
        @NotNull
        LocalDateTime couponExpiredDate,

        @Schema(description = "UNLIMITED, PRIORITY 만 요청 가능합니다.")
        @Pattern(regexp = "UNLIMITED|PRIORITY", message = "유효한 상태 값을 입력하세요.")
        @NotNull
        String couponType,

        @Schema(description = "PERCENTAGE, AMOUNT 만 요청 가능합니다.")
        @Pattern(regexp = "PERCENTAGE|AMOUNT", message = "유효한 상태 값을 입력하세요.")
        @NotNull
        String discountPolicy,

        @Schema(description = "최저 사용 금액 입니다.")
        @NotNull
        Double minDiscountAmount, // 쿠폰 사용시 최소 사용 금액

        @Schema(description = "최대 사용 금액 입니다.")
        @NotNull
        Double maxDiscountAmount, // 쿠폰 사용 할 수 있는 최대 금액

        @Schema(description = "할인 정책이 PERCENTAGE 만 요청 가능합니다.")
        Integer discountRate,

        @Schema(description = "할인 정책이 AMOUNT 만 요청 가능합니다.")
        Double discountAmount,

        @Schema(description = "쿠폰 최대 발급 수량 입니다.")
        @NotNull
        Integer totalQuantity,

        @Schema(description = "발급된 쿠폰 수량 입니다.")
        int issuedQuantity
) {
}
