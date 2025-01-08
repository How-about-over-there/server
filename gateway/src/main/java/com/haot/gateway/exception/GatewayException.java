package com.haot.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GatewayException extends RuntimeException {

  public ErrorCode errorCode;
}
