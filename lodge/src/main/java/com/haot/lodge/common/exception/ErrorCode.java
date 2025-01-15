package com.haot.lodge.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 0000: Common Error
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "0009", "Validation failed"),

    AMAZON_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0100", "Amazon Service error"),

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
    START_DATE_TOO_FAR(HttpStatus.BAD_REQUEST, "5105", "시작 날짜는 현재 날짜로부터 1년 이내여야 합니다."),
    OVERLAPPING_DATES(HttpStatus.BAD_REQUEST, "5106", "해당 기간에 생성된 날짜가 이미 존재합니다."),

    INVALID_DATE_STATUS(HttpStatus.BAD_REQUEST, "5105", "상태 타입이 유효하지 않습니다."),
    CANNOT_DELETE_SCHEDULED_DATE(HttpStatus.CONFLICT, "5106", "예약된 날짜는 삭제할 수 없습니다."),

    // 5500: Service Common
    FORBIDDEN_ACCESS_LODGE(HttpStatus.FORBIDDEN,"5501", "해당 숙소의 관리자만 접근 가능합니다."),
    UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "5500", "지원하지 않는 정렬 방식입니다."),
    FIlE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5502", "이미지 업로드에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
