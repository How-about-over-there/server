package com.haot.point.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointType {
    EARN("포인트 적립"),
    USE("포인트 사용"),
    REFUND("포인트 환불"),
    EXPIRE("포인트 만료");

    private final String description;
}
