package com.haot.point.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.application.service.PointHistoryService;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointHistoryController.class)
class PointHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PointHistoryService pointHistoryService;

    @Test
    @DisplayName("포인트 내역 단건 조회 테스트")
    void getPointHistoryById() throws Exception {
        // Given: 테스트 데이터
        String historyId = "HISTORY-UUID";
        PointHistoryResponse response = new PointHistoryResponse(historyId, "POINT-UUID", "USER-UUID",
                1000.0, "EARN", "포인트 적립", LocalDateTime.now(), "PROCESSED");

        // Mocking: PointService 동작 설정
        when(pointHistoryService.getPointHistoryById(historyId, "USER-UUID", Role.USER)).thenReturn(response);

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/api/v1/points/histories/{historyId}", historyId)
                        .header("X-User-Id", "USER-UUID")
                        .header("X-User-Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
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
        // Given: 테스트 데이터
        PointHistorySearchRequest request = new PointHistorySearchRequest();
        PageResponse<PointHistoryResponse> response = PageResponse.of(
                new PageImpl<>(List.of(
                        new PointHistoryResponse("HISTORY-UUID-1", "POINT-UUID", "USER-UUID",
                                1000.0, "EARN", "포인트 적립", LocalDateTime.now(), "PROCESSED"),
                        new PointHistoryResponse("HISTORY-UUID-2", "POINT-UUID", "USER-UUID",
                                1000.0, "EARN", "포인트 적립", LocalDateTime.now(), "PROCESSED")
                ))
        );
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "createdAt"));

        // Mocking: PointService 동작 설정
        when(pointHistoryService.getPointHistories(request, pageable, "USER-UUID", Role.USER)).thenReturn(response);

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(get("/api/v1/points/histories")
                        .header("X-User-Id", "USER-UUID")
                        .header("X-User-Role", "USER")
                        .param("page", "0") // 페이지 번호
                        .param("size", "2") // 페이지 크기
                        .param("sort", "createdAt,asc") // 정렬 기준
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
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
    @DisplayName("포인트 상태 변경 테스트")
    void changeStatusPoint() throws Exception {
        // Given: 테스트 데이터
        PointStatusRequest request = new PointStatusRequest("UUID","PROCESSED", "CANCEL_USE");
        String historyId = "HISTORY-UUID";
        PointAllResponse response = new PointAllResponse("POINT-UUID", historyId, "USER-UUID", 1000.0, 2000.0,
                "CANCEL_USE", "사용 포인트 취소", null, "PROCESSED");

        // Mocking: PointService 동작 설정
        when(pointHistoryService.updateStatusPoint(request, historyId, "USER-UUID", Role.USER)).thenReturn(response);

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(post("/api/v1/points/histories/{historyId}/status", historyId)
                        .header("X-User-Id", "USER-UUID")
                        .header("X-User-Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("9000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
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
}