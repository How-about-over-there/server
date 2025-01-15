package com.haot.reservation.common.exceptions.handler;

import com.haot.reservation.common.exceptions.ReservationException;
import com.haot.reservation.common.exceptions.DateUnavailableException;
import com.haot.reservation.common.exceptions.LodgeServiceException;
import com.haot.reservation.common.response.ApiResponse;
import com.haot.reservation.common.response.ResCodeIfs;
import com.haot.reservation.common.response.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "GlobalExceptionHandler")
@Order(value = Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LodgeServiceException.class)
  public ResponseEntity<ApiResponse<Object>> handleLodgeServiceException(LodgeServiceException e) {

    log.error(e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.ERROR(ErrorCode.INVALID_LODGE_RESPONSE_EXCEPTION));
  }

  @ExceptionHandler(DateUnavailableException.class)
  public ResponseEntity<ApiResponse<Object>> handleDateUnavailableException(
      DateUnavailableException e) {

    log.error(e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.ERROR(ErrorCode.DATE_UNAVAILABLE_EXCEPTION));
  }

  @ExceptionHandler(value = ReservationException.class)
  public ResponseEntity<ApiResponse<Object>> handleCustomCouponException(
      ReservationException reservationException) {

    log.error("{}", reservationException.resCode.getMessage());
    ResCodeIfs errorCode = reservationException.resCode;

    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(ApiResponse.ERROR(errorCode));

  }
}
