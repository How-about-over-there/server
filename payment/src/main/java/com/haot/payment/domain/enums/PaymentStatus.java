package com.haot.payment.domain.enums;

import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    READY("결제 준비"),
    VIRTUAL_ACCOUNT_ISSUED("가상계좌 발급 완료"),
    PAY_PENDING("결제 완료 대기"),
    PAID("결제 완료"),
    FAILED("결제 실패 혹은 결제 취소 실패"),
    CANCELLED("결제 취소"),
    PARTIAL_CANCELLED("결제 부분 취소"),
    CANCELLED_REQUESTED("결제 취소 요청");

    private final String description;

    public static PaymentStatus fromString(String status) {
        return Arrays.stream(PaymentStatus.values())
                .filter(enumValue -> enumValue.name().equalsIgnoreCase(status))   // 대소문자 구분 X
                .findFirst()
                .orElseThrow(() ->  new CustomPaymentException(ErrorCode.PAYMENT_STATUS_NOT_SUPPORTED));
    }
}
