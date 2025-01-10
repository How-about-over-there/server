package com.haot.reservation.common.exceptions;

import com.haot.reservation.common.response.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LodgeServiceException extends RuntimeException {

  private final ErrorCode errorCode;
}
