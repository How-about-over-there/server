package com.haot.reservation.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeignClientException extends RuntimeException {

  public String statusCode;
  public String status;
  public String message;
}
