package com.haot.reservation.common.exceptions.handler;

import com.haot.reservation.common.exceptions.FeignClientException;
import com.haot.reservation.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "FeignClientExceptionHandler")
@Order(value = Integer.MIN_VALUE)
@RestControllerAdvice
public class FeignClientExceptionHandler {

  @ExceptionHandler(value = FeignClientException.class)
  public ResponseEntity<ApiResponse<Object>> handleFeignClientException(FeignClientException e) {

    log.error("StatusCode: {}, Status: {}, Message: {}",
        e.getStatusCode(), e.getStatus(), e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.ERROR(e));
  }
}
