package com.haot.reservation.common.exceptions;

import com.haot.reservation.common.response.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeignClientException extends RuntimeException {

  public String statusCode;
  public String status;
  public String message;

  public FeignClientException(ErrorCode errorCode, String details) {
    this.statusCode = errorCode.getCode();
    this.status = "ERROR";
    this.message = errorCode.getMessage() + details;
  }
}
