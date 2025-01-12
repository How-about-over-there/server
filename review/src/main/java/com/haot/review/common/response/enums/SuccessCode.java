package com.haot.review.common.response.enums;

import com.haot.review.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements ResCodeIfs {

  UPDATE_REVIEW_SUCCESS(HttpStatus.OK, "6000", "리뷰가 성공적으로 수정 되었습니다."),
  DELETE_REVIEW_SUCCESS(HttpStatus.OK, "6000", "리뷰가 성공적으로 삭제 되었습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
