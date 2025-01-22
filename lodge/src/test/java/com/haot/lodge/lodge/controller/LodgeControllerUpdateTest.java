package com.haot.lodge.lodge.controller;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.presentation.controller.LodgeController;
import com.haot.lodge.presentation.request.lodge.LodgeUpdateRequest;
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

@WebMvcTest(LodgeController.class)
@ExtendWith(MockitoExtension.class)
public class LodgeControllerUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LodgeFacade lodgeFacade;

    @Test
    @DisplayName("숙소 수정 성공 테스트")
    void updateLodge_Success() throws Exception {
        // Given
        String userId = "UserUUID";
        Role userRole = Role.HOST;
        String lodgeId = "LodgeUUID";

        LodgeUpdateRequest updateRequest = new LodgeUpdateRequest(
                "매우 좋은 해변가 숙소",
                "업그레이드된 숙소입니다.",
                "456 Ocean View Drive",
                14,
                300.00
        );

        // When
        doNothing().when(lodgeFacade).updateLodge(userRole, userId, lodgeId, updateRequest);

        // Then
        mockMvc.perform(patch("/api/v1/lodges/{lodgeId}", lodgeId)
                        .header("X-User-Id", userId)
                        .header("X-User-Role", userRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("5000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("API 요청에 성공했습니다"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(lodgeFacade, times(1)).updateLodge(userRole, userId, lodgeId, updateRequest);
    }
}
