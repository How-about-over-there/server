package com.haot.lodge.lodgedate.controller;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.lodge.application.facade.LodgeDateFacade;
import com.haot.lodge.presentation.controller.LodgeDateController;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateUpdateRequest;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LodgeDateController.class)
@ExtendWith(MockitoExtension.class)
public class LodgeDateControllerUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LodgeDateFacade lodgeDateFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("숙소 날짜 가격 수정 성공 테스트")
    void updatePrice_Success() throws Exception {
        // Given
        LodgeDateUpdateRequest request = new LodgeDateUpdateRequest(12000.0);
        Role role = Role.HOST;
        String userId = "UserUUID";
        String dateId = "DateUUID";

        // When
        doNothing().when(lodgeDateFacade).updatePrice(role, userId, dateId, request.price());

        // Then
        mockMvc.perform(patch("/api/v1/lodge-dates/{dateId}", dateId)
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("5000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("API 요청에 성공했습니다"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(lodgeDateFacade, times(1))
                .updatePrice(eq(role), eq(userId), eq(dateId), eq(request.price()));
    }
}
