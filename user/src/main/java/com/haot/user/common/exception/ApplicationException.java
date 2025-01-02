package com.haot.user.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

  public ErrorCode errorCode;
}
