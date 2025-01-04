package com.haot.coupon.common.response.enums;

import com.haot.coupon.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

    // 0000: Common Error
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "0000", "Unknown Server Error"),
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "0009", "Validation failed"),

    // 쿠폰 exception
    DISCOUNT_RATE_EXCEPTION(HttpStatus.BAD_REQUEST, "4002", "할인율은 1 ~ 100 사이의 숫자여야 됩니다."),
    RESERVATION_STATUS_NOT_MATCH(HttpStatus.BAD_REQUEST, "4003", "reservationStatus NotMatch"),
    INSUFFICIENT_DATE_DIFFERENCE(HttpStatus.BAD_REQUEST, "4004", "시작날짜와 만료 날짜가 최소 하루이상 차이가 나야합니다."),
    TOO_MANY_DISCOUNT_POLICY(HttpStatus.BAD_REQUEST, "4005", "할인 정책은 하나만 적용할 수 있습니다."),
    DISCOUNT_POLICY_NOT_MATCH(HttpStatus.BAD_REQUEST, "4006", "할인 정책이 맞지 않습니다."),
    WRONG_DISCOUNT_AMOUNT(HttpStatus.BAD_REQUEST, "4007", "할인 금액 설정이 맞지 않거나 없습니다."),
    DISCOUNT_EXCEEDS_MIN_AMOUNT(HttpStatus.BAD_REQUEST, "4008", "할인 금액이 최소 사용 가능 금액을 초과합니다."),
    DISCOUNT_EXCEEDS_MAX_AMOUNT(HttpStatus.BAD_REQUEST, "4009", "할인 금액이 최대 사용 가능 금액을 초과합니다."),

    WRONG_TOTAL_QUANTITY(HttpStatus.BAD_REQUEST, "4010", "쿠폰 최대 발급 수량이 정책에 맞지 않습니다."),
    COUPON_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, "4011", "쿠폰 타입이 맞지 않습니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "4012", "Coupon not found."),
    EXIST_PRIORITY_COUPON_EVENTS(HttpStatus.BAD_REQUEST, "4013", "쿠폰이 이미 있는 이벤트의 선착순 쿠폰입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
