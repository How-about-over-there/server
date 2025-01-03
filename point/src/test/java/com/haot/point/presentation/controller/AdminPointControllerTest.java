package com.haot.point.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.point.application.dto.request.PointUpdateRequest;
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

@WebMvcTest(AdminPointController.class)
class AdminPointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포인트 수정 테스트")
    void updatePoint() throws Exception{
        // Given: 테스트 데이터
        PointUpdateRequest request = new PointUpdateRequest("USER-UUID", 1000.0);
        String pointId = "POINT-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(put("/admin/v1/points/{pointId}", pointId)
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
    @DisplayName("포인트 삭제 테스트")
    void deletePoint() throws Exception {
        // Given: 테스트 데이터
        String pointId = "POINT-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(delete("/admin/v1/points/{pointId}", pointId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andReturn(); // 호출 결과를 MvcResult 로 반환

    }

    @Test
    @DisplayName("포인트 단건 조회 테스트")
    void getPointById() throws Exception {
        // Given: 테스트 데이터
        String pointId = "POINT-UUID";

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/admin/v1/points/{pointId}", pointId)
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
    @DisplayName("포인트 전체 조회 및 검색 테스트")
    void getPoints() throws Exception {
        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/admin/v1/points")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].pointId").exists())
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