package com.haot.payment.common.exception.enums;

import com.haot.payment.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

    // 0000: Common Error
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "0000", "예기치 못한 오류가 발생했습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "0001", "존재하지 않는 리소스입니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "0002", "올바르지 않은 요청 값입니다."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "0003", "권한이 없는 요청입니다."),
    ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, "0004", "이미 삭제된 리소스입니다."),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "0005", "인가되지 않는 요청입니다."),
    ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "0006", "이미 존재하는 리소스입니다."),
    INVALID_SORT_EXCEPTION(HttpStatus.BAD_REQUEST, "0007", "올바르지 않은 정렬 값입니다."),
    SEARCH_LOG_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "0008", "존재하지 않는 검색 로그입니다."),
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "0009", "입력 데이터 유효성 검증에 실패했습니다."),

    // 8000: Payment Error
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "8001", "결제 정보를 찾을 수 없습니다."),
    PAYMENT_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "8002", "지원하지 않는 결제 방식입니다."),
    PAYMENT_STATUS_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "8003", "지원하지 않는 결제 상태입니다."),
    PORTONE_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "8004", "외부 결제 시스템 호출이 실패했습니다."),
    PAYMENT_PRICE_MISMATCH(HttpStatus.BAD_REQUEST, "8005", "결제 요청 금액과 실제 결제 금액이 일치하지 않습니다."),
    INVALID_PAYMENT_STATUS(HttpStatus.BAD_REQUEST, "8006", "결제가 완료되지 않았습니다."),
    PAYMENT_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "8007", "이미 처리된 결제입니다."),
    USER_NOT_MATCHED(HttpStatus.BAD_REQUEST,"8008" , "유저 ID 가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
