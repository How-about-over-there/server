package com.haot.lodge.domain.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationStatus {
    EMPTY("예약 없음"),
    WAITING("예약 대기"),
    COMPLETE("예약 완료");

    private final String description;
}
