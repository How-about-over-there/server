package com.haot.point.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.point.application.dto.request.history.PointHistoryCreateRequest;
import com.haot.point.application.dto.request.history.PointHistoryUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminPointHistoryController.class)
class AdminPointHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포인트 내역 생성 테스트")
    void createPointHistory() throws Exception {
        // Given: 테스트 데이터
        PointHistoryCreateRequest request = new PointHistoryCreateRequest("POINT-UUID", 1000.0, "EARN", "RESERVATION-UUID USE POINT");

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(post("/admin/v1/points/histories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.historyId").exists())
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
    @DisplayName("포인트 내역 수정 테스트")
    void updatePointHistory() throws Exception {
        // Given: 테스트 데이터
        PointHistoryUpdateRequest request = new PointHistoryUpdateRequest("POINT-UUID", 1000.0, "EARN", "RESERVATION-UUID USE POINT");
        String historyId = "HISTORY-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(put("/admin/v1/points/histories/{historyId}", historyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
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
    @DisplayName("포인트 내역 삭제 테스트")
    void deletePointHistory() throws Exception {
        // Given: 테스트 데이터
        String historyId = "HISTORY-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(delete("/admin/v1/points/histories/{historyId}", historyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
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
    @DisplayName("포인트 내역 단건 조회 테스트")
    void getPointHistoryById() throws Exception {
        // Given: 테스트 데이터
        String historyId = "HISTORY-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/admin/v1/points/histories/{historyId}", historyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.historyId").value(historyId))
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
    @DisplayName("포인트 내역 전체 조회 및 검색 테스트")
    void getPointHistories() throws Exception {
        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/admin/v1/points/histories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].historyId").exists())
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