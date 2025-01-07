package com.haot.payment.domain.enums;

import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CARD("카드"),
    TRANSFER("계좌 이체");

    private final String description;

    public static PaymentMethod fromString(String method) {
        return Arrays.stream(PaymentMethod.values())
                .filter(enumValue -> enumValue.name().equalsIgnoreCase(method))   // 대소문자 구분 X
                .findFirst()
                .orElseThrow(() -> new CustomPaymentException(ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED));
    }
}
