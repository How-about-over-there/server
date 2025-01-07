package com.haot.coupon.common.response.enums;

import com.haot.coupon.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements ResCodeIfs {

    CREATE_COUPON_SUCCESS(HttpStatus.CREATED, "4000", "쿠폰이 성공적으로 생성되었습니다."),
    CREATE_EVENT_SUCCESS(HttpStatus.CREATED, "4000", "이벤트가 성공적으로 생성되었습니다."),
    CUSTOMER_ISSUED_COUPON_SUCCESS(HttpStatus.OK, "4000", "사용자가 쿠폰을 성공적으로 발급하였습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
