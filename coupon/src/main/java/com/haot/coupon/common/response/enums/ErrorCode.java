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
    DISCOUNT_RATE_EXCEPTION(HttpStatus.BAD_REQUEST, "4002", "wrong discountRate");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
