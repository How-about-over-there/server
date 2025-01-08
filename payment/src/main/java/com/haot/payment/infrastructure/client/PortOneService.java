package com.haot.payment.infrastructure.client;

import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import com.haot.payment.infrastructure.client.dto.PortOneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j(topic = "PortOneService")
@Service
public class PortOneService {

    private static final String API_BASE_URL = "https://api.portone.io";
    private final String apiSecret;

    public PortOneService(@Value("${portone.api.secret}") String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public PortOneResponse getPaymentData(String paymentId) {
        RestTemplate restTemplate = new RestTemplate();

        // 포트원 결제 조회 API
        String url = API_BASE_URL + "/payments/" + paymentId;

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        String authHeader = "PortOne " + apiSecret;

        headers.set("Authorization", authHeader); // Authorization 헤더 설정
        headers.set("Content-Type", "application/json"); // JSON 요청 헤더 추가

        log.info("authHeader ::::: {}", authHeader);

        // HTTP 요청 생성
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PortOneResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, PortOneResponse.class);
            return response.getBody();
        } catch (Exception e) {
            // 오류 출력
            log.error("포트원 결제 조회 API 호출 실패 ::::: {}", e.getMessage(), e);
            throw new CustomPaymentException(ErrorCode.PORTONE_API_CALL_FAILED);
        }
    }
}
