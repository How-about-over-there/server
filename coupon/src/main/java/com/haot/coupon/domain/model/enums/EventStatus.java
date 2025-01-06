package com.haot.coupon.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventStatus {

    DEFAULT("기본값"),
    // 쿠폰 발급 수량과 무관하게 기한이 지나 종료
    EXPIRED("기한이 지나 종료"),
    OUT_OF_STOCK("쿠폰 발급 수량으로 인한 종료"),
    MANUALLY_CLOSED("관리자가 종료")
    ;

    private final String description;

}
