package com.haot.coupon.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponStatus {

    DISTRIBUTED("쿠폰 기본 상태"),
    USED("사용"),
    UNUSED("미사용");

    private final String description;
}
