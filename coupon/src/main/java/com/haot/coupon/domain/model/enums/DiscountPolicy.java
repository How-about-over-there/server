package com.haot.coupon.domain.model.enums;

import com.haot.coupon.common.exceptions.CustomCouponException;
import com.haot.coupon.common.response.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DiscountPolicy {

    PERCENTAGE("퍼센트 할인"),
    AMOUNT("금액 할인");

    private final String description;

    public static DiscountPolicy checkDiscountPolicy(String status) {
        return Stream.of(DiscountPolicy.values())
                .filter(discountPolicy -> discountPolicy.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new CustomCouponException(ErrorCode.DISCOUNT_POLICY_NOT_MATCH));
    }

}
