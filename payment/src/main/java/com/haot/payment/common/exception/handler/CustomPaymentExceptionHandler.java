package com.haot.payment.common.exception.handler;

import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.response.ApiResponse;
import com.haot.payment.common.response.ResCodeIfs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "CustomPaymentExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE) // 최우선 처리
public class CustomPaymentExceptionHandler {

    @ExceptionHandler(CustomPaymentException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomPaymentException(CustomPaymentException ex) {
        ResCodeIfs resCode = ex.getResCode();
        return ResponseEntity
                .status(resCode.getHttpStatus())
                .body(ApiResponse.error(resCode));
    }
}
