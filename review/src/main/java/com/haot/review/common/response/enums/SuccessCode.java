package com.haot.review.common.response.enums;

import com.haot.review.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements ResCodeIfs {

  DELETE_REVIEW_SUCCESS(HttpStatus.CREATED, "6000", "리뷰가 성공적으로 삭제 되었습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
