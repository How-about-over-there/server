package com.haot.coupon.application.dto.response.coupons;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "쿠폰 단건 조회 RESPONSE DTO")
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
