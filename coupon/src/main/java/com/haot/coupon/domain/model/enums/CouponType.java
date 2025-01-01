package com.haot.coupon.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponType {

    UNLIMITED("무제한"),
    PRIORITY("선착순");

    private final String description;
}
