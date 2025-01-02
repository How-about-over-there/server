package com.haot.coupon.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventStatus {

    IN_PROGRESS("진행중"),
    END("종료"),
    AWAITING("대기중");

    private final String description;

}
