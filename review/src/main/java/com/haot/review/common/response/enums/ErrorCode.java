package com.haot.review.common.response.enums;

import com.haot.review.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResCodeIfs {

  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "6001", "Review not found.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
