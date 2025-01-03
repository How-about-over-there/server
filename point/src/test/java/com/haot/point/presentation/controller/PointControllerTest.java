package com.haot.point.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.point.application.dto.request.PointCreateRequest;
import com.haot.point.application.dto.request.PointTransactionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포인트 생성 테스트")
    void createPoint() throws Exception {
        // Given: 테스트 데이터
        PointCreateRequest request = new PointCreateRequest("USER-UUID", 1000.0);

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(post("/api/v1/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.pointId").exists())
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
    @DisplayName("본인 포인트 단건 조회 테스트")
    void getPointById() throws Exception {
        // Given: 테스트 데이터
        String pointId = "POINT-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/api/v1/points/{pointId}", pointId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.pointId").value(pointId))
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
    @DisplayName("포인트 적립 테스트")
    void earnPoint() throws Exception {
        // Given: 테스트 데이터
        PointTransactionRequest request = new PointTransactionRequest(1000.0, "RESERVATION-UUID EARN POINT");
        String pointId = "POINT-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(post("/api/v1/points/{pointId}/earn", pointId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.pointId").value(pointId))
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
    @DisplayName("포인트 사용 테스트")
    void usePoint() throws Exception {
        // Given: 테스트 데이터
        PointTransactionRequest request = new PointTransactionRequest(1000.0, "RESERVATION-UUID USE POINT");
        String pointId = "POINT-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(post("/api/v1/points/{pointId}/use", pointId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.pointId").value(pointId))
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