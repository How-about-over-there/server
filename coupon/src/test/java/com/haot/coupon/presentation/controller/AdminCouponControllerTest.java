package com.haot.coupon.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.coupon.application.dto.request.coupons.CouponCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponCreateResponse;
import com.haot.coupon.application.service.AdminCouponService;
import com.haot.submodule.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j(topic = "AdminCouponControllerTest")
@WebMvcTest(AdminCouponController.class)
class AdminCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminCouponService adminCouponService;

    @Test
    @DisplayName("무제한 쿠폰 생성 성공 API")
    void createCoupon() throws Exception {

        LocalDateTime couponAvailableDate = LocalDateTime.now().plusSeconds(10);
        LocalDateTime couponExpireDate = LocalDateTime.now().plusDays(3);

        // Given: 테스트 데이터
        CouponCreateRequest request = new CouponCreateRequest("무제한 쿠폰 test",
                couponAvailableDate, couponExpireDate, "UNLIMITED",
                "AMOUNT", 30000.0, 70000.0, null,
                5000.0, -1, 0);
        CouponCreateResponse response = new CouponCreateResponse("TestUnlimitedCouponUUID");

        // Mocking: adminCouponService 동작 설정
        when(adminCouponService.create(request)).thenReturn(response);

        // When: API 호출 및 결과 받기
        MvcResult result = mockMvc.perform(post("/admin/v1/coupons")
                        //.header("X-User-Id", "USER-UUID")
                        //.header("X-User-Role", "USER")
                        //.header("Authorization", "BearerToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("4000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.couponId").exists())
                .andReturn(); // 호출 결과를 MvcResult 로 반환

        // Then: 요청 결과 확인
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        // JSON 포맷팅
        String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                objectMapper.readTree(responseBody) // JSON 트리로 파싱
        );
        // 응답 Body 출력
        log.info("응답 데이터: {}", formattedJson);
    }

}