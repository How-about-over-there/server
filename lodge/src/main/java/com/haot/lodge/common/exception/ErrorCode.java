package com.haot.lodge.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 0000: Common Error
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "0009", "Validation failed"),

    // 5000: Lodge Error
    LODGE_NOT_FOUND(HttpStatus.NOT_FOUND, "5001", "일치하는 숙소 정보를 찾을 수 없습니다."),
    LODGE_RULE_NOT_FOUND(HttpStatus.NOT_FOUND, "5002", "일치하는 숙소 규칙 정보를 찾을 수 없습니다."),
    ALREADY_EXIST_LODGE_NAME(HttpStatus.CONFLICT, "5003","같은 이름의 숙소가 이미 존재합니다."),

    // 5100: LodgeDate Error
    LODGE_DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "5100", "일치하는 숙소 날짜를 찾을 수 없습니다."),
    START_DATE_IN_PAST(HttpStatus.BAD_REQUEST, "5101", "시작 날짜는 현재 날짜 이후여야 합니다."),
    START_DATE_AFTER_END_DATE(HttpStatus.BAD_REQUEST, "5102", "시작 날짜는 종료 날짜보다 이전이어야 합니다."),
    DATE_RANGE_TOO_SHORT(HttpStatus.BAD_REQUEST, "5103", "날짜 범위는 최소 30일 이상이어야 합니다."),
    DATE_RANGE_TOO_LONG(HttpStatus.BAD_REQUEST, "5104", "날짜 범위는 최대 365일을 초과할 수 없습니다."),
    INVALID_DATE_STATUS(HttpStatus.BAD_REQUEST, "5105", "상태 타입이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
