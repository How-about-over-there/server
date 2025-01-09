package com.haot.review.common.exceptions;

import com.haot.review.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomReviewException extends RuntimeException{
  public ResCodeIfs resCode;
}
