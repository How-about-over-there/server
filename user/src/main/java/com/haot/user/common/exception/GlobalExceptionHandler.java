package com.haot.user.common.exception;

import com.haot.user.common.response.ApiResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(UserException.class)
  protected ResponseEntity<ApiResponse<Void>> handleUserException(UserException e) {
    log.error("{} {}", e, e.getErrorCode().toString());
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(ApiResponse.fail(e.getErrorCode()));
  }

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
    log.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.fail(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> methodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error(e.getMessage());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.fail(ErrorCode.INVALID_VALUE_EXCEPTION,
            Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
  }
}
