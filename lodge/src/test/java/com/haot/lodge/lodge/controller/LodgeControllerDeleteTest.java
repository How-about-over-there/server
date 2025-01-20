package com.haot.lodge.lodge.controller;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.presentation.controller.LodgeController;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LodgeController.class)
@ExtendWith(MockitoExtension.class)
public class LodgeControllerDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LodgeFacade lodgeFacade;

    @Test
    @DisplayName("숙소 삭제 성공 테스트")
    void deleteLodge_Success() throws Exception {
        // Given
        String userId = "UserUUID";
        Role userRole = Role.HOST;
        String lodgeId = "LodgeUUID";

        // When
        doNothing().when(lodgeFacade).deleteLodge(userRole, userId, lodgeId);

        // Then
        mockMvc.perform(delete("/api/v1/lodges/{lodgeId}", lodgeId)
                        .header("X-User-Id", userId)
                        .header("X-User-Role", userRole.name())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("5000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("API 요청에 성공했습니다"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(lodgeFacade, times(1)).deleteLodge(userRole, userId, lodgeId);
    }

}
