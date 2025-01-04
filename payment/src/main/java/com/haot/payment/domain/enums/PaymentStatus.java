package com.haot.payment.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    READY("결제 준비"),
    VIRTUAL_ACCOUNT_ISSUED("가상계좌 발급 완료"),
    PAY_PENDING("결제 완료 대기"),
    PAID("결제 완료"),
    FAILED("결제 실패"),
    CANCELLED("결제 취소"),
    PARTIAL_CANCELLED("결제 부분 취소");

    private final String description;
}
