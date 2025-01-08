package com.haot.point.common.exception.handler;

import com.haot.point.common.exception.CustomPointException;
import com.haot.point.common.response.ApiResponse;
import com.haot.point.common.response.ResCodeIfs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "CustomPointExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE) // 최우선 처리
public class CustomPointExceptionHandler {

    @ExceptionHandler(CustomPointException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomPaymentException(CustomPointException ex) {
        ResCodeIfs resCode = ex.getResCode();
        return ResponseEntity
                .status(resCode.getHttpStatus())
                .body(ApiResponse.error(resCode));
    }
}