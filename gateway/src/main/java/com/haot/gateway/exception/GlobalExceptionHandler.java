package com.haot.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

  private final ObjectMapper objectMapper;

  public GlobalExceptionHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // JSON 응답 생성
    String errorMessage = "Internal server error";
    String statusCode = "0000";
    if (ex instanceof GatewayException) {
      errorMessage = ((GatewayException) ex).getErrorCode().getMessage();
      statusCode = ((GatewayException) ex).getErrorCode().getCode();
    }

    Map<String, String> errorResponse = Map.of(
        "statusCode", statusCode,
        "status", "ERROR",
        "message", errorMessage
    );
    try {
      // ObjectMapper로 JSON 직렬화
      byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);

      // DataBuffer로 응답에 추가
      DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
      return exchange.getResponse().writeWith(Mono.just(buffer));
    } catch (Exception e) {
      // JSON 직렬화 실패 시 추가 에러 처리
      return Mono.error(e);
    }
  }

}
