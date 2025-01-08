package com.haot.payment.common.exception;

import com.haot.payment.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomPaymentException extends RuntimeException {
    private final ResCodeIfs resCode;
}
