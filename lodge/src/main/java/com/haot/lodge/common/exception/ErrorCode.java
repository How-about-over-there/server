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
    ALREADY_EXIST_REVIEW(HttpStatus.CONFLICT, "5001","같은 이름의 숙소가 이미 존재합니다."),

    // 5100: LodgeDate Error
    START_DATE_IN_PAST(HttpStatus.BAD_REQUEST, "5101", "시작 날짜는 현재 날짜 이후여야 합니다."),
    START_DATE_AFTER_END_DATE(HttpStatus.BAD_REQUEST, "5102", "시작 날짜는 종료 날짜보다 이전이어야 합니다."),
    DATE_RANGE_TOO_SHORT(HttpStatus.BAD_REQUEST, "5103", "날짜 범위는 최소 30일 이상이어야 합니다."),
    DATE_RANGE_TOO_LONG(HttpStatus.BAD_REQUEST, "5104", "날짜 범위는 최대 365일을 초과할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
