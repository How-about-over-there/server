package com.haot.point.domain.enums;

import com.haot.point.common.exception.CustomPointException;
import com.haot.point.common.exception.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PointType {
    EARN("포인트 적립"),
    USE("포인트 사용"),
    REFUND("포인트 환불"),
    EXPIRE("포인트 만료");

    private final String description;

    public static PointType fromString(String type) {
        return Arrays.stream(PointType.values())
                .filter(enumValue -> enumValue.name().equalsIgnoreCase(type))   // 대소문자 구분 X
                .findFirst()
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_TYPE_NOT_SUPPORTED));
    }
}
