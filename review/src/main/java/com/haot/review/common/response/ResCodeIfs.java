package com.haot.review.common.response;

import org.springframework.http.HttpStatus;

public interface ResCodeIfs {

  HttpStatus getHttpStatus();

  String getCode();

  String getMessage();

}
