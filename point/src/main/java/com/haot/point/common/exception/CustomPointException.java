package com.haot.point.common.exception;

import com.haot.point.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomPointException extends RuntimeException {
    private final ResCodeIfs resCode;
}