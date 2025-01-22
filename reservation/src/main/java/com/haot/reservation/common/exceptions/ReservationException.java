package com.haot.reservation.common.exceptions;

import com.haot.reservation.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationException extends RuntimeException{
  public ResCodeIfs resCode;
}
