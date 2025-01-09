package com.haot.review.common.exceptions.handler;

import com.haot.review.common.exceptions.CustomReviewException;
import com.haot.review.common.response.ApiResponse;
import com.haot.review.common.response.ResCodeIfs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "CustomReviewExceptionHandler")
@RestControllerAdvice
public class CustomReviewExceptionHandler {

  @ExceptionHandler(value = CustomReviewException.class)
  public ResponseEntity<ApiResponse<Object>> handleCustomCouponException(
      CustomReviewException customCouponException) {

    log.error("{}", customCouponException.resCode.getMessage());
    ResCodeIfs errorCode = customCouponException.resCode;

    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(ApiResponse.ERROR(errorCode));

  }

}
