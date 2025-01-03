package com.haot.point.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointStatus {
    PENDING("포인트 처리 대기 상태"),
    PROCESSED("포인트 처리 완료 상태");

    private final String description;
}
