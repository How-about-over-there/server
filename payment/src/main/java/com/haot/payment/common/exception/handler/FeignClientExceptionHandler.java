package com.haot.payment.common.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FeignClientExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException e) {
        String errorResponse = e.contentUTF8(); // FeignException 에서 응답 바디 추출

        log.error("FeignException 발생 ::::: 상태 코드: {} 응답 바디: {}", e.status(), errorResponse);

        // 응답 바디가 텍스트이거나 비어 있는 경우 JSON 응답 생성
        if (errorResponse == null || !isJson(errorResponse)) {
            errorResponse = "{\"message\":\"예약 서비스 호출에 실패하였습니다.\",\"status\":\"ERROR\",\"statusCode\":\"8009\"}";
        }
        // Content-Type 을 application/json 으로 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json"); // JSON 요청 헤더 추가

        // FeignException 의 상태 코드와 응답 바디를 그대로 클라이언트에 반환
        return ResponseEntity
                .status(e.status()) // HTTP 상태 코드 그대로 사용
                .headers(headers)   // Json 헤더 추가
                .body(errorResponse); // 응답 바디 그대로 반환
    }

    private boolean isJson(String content) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(content); // JSON 파싱 시도
            return true; // JSON 형식이면 true 반환
        } catch (Exception e) {
            return false; // JSON 형식이 아니면 false 반환
        }
    }
}
