package com.haot.payment.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.payment.application.dto.request.PaymentCancelRequest;
import com.haot.payment.application.dto.request.PaymentCreateRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.application.service.PaymentService;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 생성 성공 테스트")
    void createPayment() throws Exception {
        // Given: 테스트 데이터
        PaymentCreateRequest request = new PaymentCreateRequest("USER-UUID", "RESERVATION-UUID", 100000.0, "CARD");
        PaymentResponse response = new PaymentResponse("PAYMENT-UUID", "USER-UUID", "RESERVATION-UUID", null, 100000.0,null, "CARD", "READY");

        // Mocking: PaymentService 동작 설정
        when(paymentService.createPayment(request, "USER-UUID", Role.USER)).thenReturn(response);

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(post("/api/v1/payments")
                        .header("X-User-Id", "USER-UUID")
                        .header("X-User-Role", "USER")
                        .header("Authorization", "BearerToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("8000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.payment.paymentId").exists())
                .andReturn(); // 호출 결과를 MvcResult 로 반환

        // Then: 요청 결과 확인
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        // JSON 포맷팅
        String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                objectMapper.readTree(responseBody) // JSON 트리로 파싱
        );
        // 응답 Body 출력
        System.out.println("응답 데이터: " + formattedJson);
    }

    @Test
    @DisplayName("결제 단건 조회 테스트")
    void getPaymentById() throws Exception {
        // Given: 테스트 데이터
        String paymentId = "PAYMENT-UUID1";
        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/api/v1/payments/{paymentId}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("8000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.paymentId").value(paymentId))
                .andReturn(); // 호출 결과를 MvcResult 로 반환

        // Then: 요청 결과 확인
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        // JSON 포맷팅
        String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                objectMapper.readTree(responseBody) // JSON 트리로 파싱
        );
        // 응답 Body 출력
        System.out.println("응답 데이터: " + formattedJson);
    }

    @Test
    @DisplayName("결제 전체 조회 및 검색 테스트")
    void getPayments() throws Exception {
        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("8000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].paymentId").exists())
                .andReturn(); // 호출 결과를 MvcResult 로 반환

        // Then: 요청 결과 확인
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        // JSON 포맷팅
        String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                objectMapper.readTree(responseBody) // JSON 트리로 파싱
        );
        // 응답 Body 출력
        System.out.println("응답 데이터: " + formattedJson);
    }

    @Test
    @DisplayName("결제 취소 테스트")
    void cancelPayment() throws Exception {
        // Given: 테스트 데이터
        PaymentCancelRequest request = new PaymentCancelRequest("단순 변심");
        String paymentId = "PAYMENT-UUID1";
        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/api/v1/payments/{paymentId}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("8000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.paymentId").value(paymentId))
                .andReturn(); // 호출 결과를 MvcResult 로 반환

        // Then: 요청 결과 확인
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        // JSON 포맷팅
        String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                objectMapper.readTree(responseBody) // JSON 트리로 파싱
        );
        // 응답 Body 출력
        System.out.println("응답 데이터: " + formattedJson);
    }
}
