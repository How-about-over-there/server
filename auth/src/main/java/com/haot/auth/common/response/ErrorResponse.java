package com.haot.auth.common.response;

import com.haot.auth.common.exception.ErrorCode;
import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timestamp,
    String code,
    String message
) {

  public ErrorResponse(ErrorCode errorcode) {
    this(LocalDateTime.now(), errorcode.getCode(), errorcode.getMessage());
  }

  public ErrorResponse(String message) {
    this(LocalDateTime.now(), ErrorCode.INTERNAL_SERVER_EXCEPTION.getCode(), message);
  }

  public ErrorResponse(ErrorCode errorcode, String message) {
    this(LocalDateTime.now(), errorcode.getCode(), message);
  }
}