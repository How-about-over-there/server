package com.haot.coupon.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountPolicy {

    PERCENTAGE("퍼센트 할인"),
    AMOUNT("금액 할인");

    private final String description;

}
