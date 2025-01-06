package com.haot.lodge.common.exception;

import lombok.Getter;

@Getter
public class LodgeException extends RuntimeException {
    private final ErrorCode errorCode;

    public LodgeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
