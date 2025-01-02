package com.haot.user.presentation.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc; // MockMvc 주입

  @Autowired
  private ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper

  @Test
  void validateLoginInformation() throws Exception {
    MvcResult result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/users/validation")
        )
        .andExpectAll(
            MockMvcResultMatchers.status().isOk()
        )
        .andReturn();// 요청 결과 반환

    // 요청 결과 확인 (응답 Body 출력)
    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    // JSON 포맷팅
    String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        objectMapper.readTree(responseBody) // JSON 트리로 파싱
    );
    // 결과 출력
    System.out.println("Formatted JSON Response:\n" + formattedJson);
  }

  @Test
  void validateUser() throws Exception {
    MvcResult result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/users/1/validation")
        )
        .andExpectAll(
            MockMvcResultMatchers.status().isOk()
        )
        .andReturn();// 요청 결과 반환

    // 요청 결과 확인 (응답 Body 출력)
    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    // JSON 포맷팅
    String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        objectMapper.readTree(responseBody) // JSON 트리로 파싱
    );
    // 결과 출력
    System.out.println("Formatted JSON Response:\n" + formattedJson);
  }

  @Test
  void getMyInfo() throws Exception {
    MvcResult result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/users/me")
        )
        .andExpectAll(
            MockMvcResultMatchers.status().isOk()
        )
        .andReturn();// 요청 결과 반환

    // 요청 결과 확인 (응답 Body 출력)
    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    // JSON 포맷팅
    String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        objectMapper.readTree(responseBody) // JSON 트리로 파싱
    );
    // 결과 출력
    System.out.println("Formatted JSON Response:\n" + formattedJson);
  }

  @Test
  void updateMyInfo() throws Exception {
    MvcResult result = mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/v1/users/me")
        )
        .andExpectAll(
            MockMvcResultMatchers.status().isOk()
        )
        .andReturn();// 요청 결과 반환

    // 요청 결과 확인 (응답 Body 출력)
    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    // JSON 포맷팅
    String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        objectMapper.readTree(responseBody) // JSON 트리로 파싱
    );
    // 결과 출력
    System.out.println("Formatted JSON Response:\n" + formattedJson);
  }

  @Test
  void deleteMyInfo() throws Exception {

    MvcResult result = mockMvc.perform(
          RestDocumentationRequestBuilders.delete("/api/v1/users/me")
      )
      .andExpectAll(
          MockMvcResultMatchers.status().isOk()
      )
      .andReturn();// 요청 결과 반환

    // 요청 결과 확인 (응답 Body 출력)
    String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    // JSON 포맷팅
    String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
        objectMapper.readTree(responseBody) // JSON 트리로 파싱
    );
    // 결과 출력
    System.out.println("Formatted JSON Response:\n" + formattedJson);
  }
}