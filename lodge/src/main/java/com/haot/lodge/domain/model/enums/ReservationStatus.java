package com.haot.lodge.domain.model.enums;

import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationStatus {
    UNSELECTABLE("선택 불가능"),
    EMPTY("예약 없음"),
    WAITING("예약 대기"),
    COMPLETE("예약 완료");

    private final String description;

    public static ReservationStatus fromString(String request){
        request = request.toUpperCase();
        return switch (request){
            case "UNSELECTABLE" -> UNSELECTABLE;
            case "EMPTY" -> EMPTY;
            case "WAITING" -> WAITING;
            case "COMPLETE" -> COMPLETE;
            default -> throw new LodgeException(ErrorCode.INVALID_DATE_STATUS);
        };
    }
}
