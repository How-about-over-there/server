package com.haot.coupon.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationCouponStatus {

    CANCEL("쿠폰 적용 취소"),
    COMPLETED("쿠폰 적용 완료"),
    PREEMPTION("쿠폰 선점"),
    EXPIRED("쿠폰 만료");

    private final String description;
}
