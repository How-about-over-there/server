package com.haot.payment.infrastructure.client;

import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import com.haot.payment.infrastructure.client.dto.request.PortOneCancelRequest;
import com.haot.payment.infrastructure.client.dto.response.PortOneCancelResponse;
import com.haot.payment.infrastructure.client.dto.response.PortOneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

        // HTTP 요청 생성
        HttpHeaders headers = settingHeader();
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

    public PortOneCancelResponse cancelPayment(PortOneCancelRequest cancelRequest, String paymentId) {
        // 포트원 결제 취소 API
        String url = API_BASE_URL + "/payments/" + paymentId + "/cancel";

        // HTTP 요청 생성
        HttpHeaders headers = settingHeader();
        HttpEntity<PortOneCancelRequest> entity = new HttpEntity<>(cancelRequest ,headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PortOneCancelResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, PortOneCancelResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // 오류 출력
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                log.error("포트원 결제 취소 API - 이미 취소된 결제: {}", e.getMessage());
                throw new CustomPaymentException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
            }
            log.error("포트원 결제 취소 API 호출 실패 ::::: {}", e.getMessage(), e);
            throw new CustomPaymentException(ErrorCode.PORTONE_API_CALL_FAILED);
        }
    }

    // 헤더 설정
    private HttpHeaders settingHeader() {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = "PortOne " + apiSecret;

        headers.set("Authorization", authHeader); // Authorization 헤더 설정
        headers.set("Content-Type", "application/json"); // JSON 요청 헤더 추가

        log.info("authHeader ::::: {}", authHeader);
        return headers;
    }
}
