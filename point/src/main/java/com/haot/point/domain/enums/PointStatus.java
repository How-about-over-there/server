package com.haot.point.domain.enums;

import com.haot.point.common.exception.CustomPointException;
import com.haot.point.common.exception.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PointStatus {
    PENDING("포인트 처리 대기 상태"),
    PROCESSED("포인트 처리 완료 상태"),
    ROLLBACK("포인트 롤백 상태"),
    CANCELLED("포인트 적립/사용이 취소된 상태");

    private final String description;

    public static PointStatus fromString(String status) {
        return Arrays.stream(PointStatus.values())
                .filter(enumValue -> enumValue.name().equalsIgnoreCase(status))   // 대소문자 구분 X
                .findFirst()
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_STATUS_NOT_SUPPORTED));
    }
}
