package com.haot.lodge.lodge.controller;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeImageDto;
import com.haot.lodge.application.dto.LodgeRuleDto;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.response.LodgeReadOneResponse;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.presentation.controller.LodgeController;
import com.haot.lodge.util.TestDtoFixture;
import java.util.List;
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
public class LodgeControllerReadOneTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LodgeFacade lodgeFacade;


    @Test
    @DisplayName("숙소 단건 조회 성공 테스트")
    void readOneLodge_Success() throws Exception {
        // Given
        String lodgeId = "LodgeUUID";

        LodgeDto lodgeDto = TestDtoFixture.createMockLodgeDto();
        LodgeImageDto imageDto = TestDtoFixture.createMockLodgeImageDto();
        LodgeRuleDto ruleDto = TestDtoFixture.createMockLodgeRuleDto();

        LodgeReadOneResponse response = new LodgeReadOneResponse(
                lodgeDto,
                List.of(imageDto),
                ruleDto
        );

        ApiResponse<LodgeReadOneResponse> expectedResponse = ApiResponse.success(response);

        // When
        when(lodgeFacade.readLodge(lodgeId)).thenReturn(response);

        // Then
        mockMvc.perform(get("/api/v1/lodges/{lodgeId}", lodgeId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("5000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("API 요청에 성공했습니다"))
                .andExpect(jsonPath("$.data.lodge.id").value(lodgeDto.id()))
                .andExpect(jsonPath("$.data.lodge.hostId").value(lodgeDto.hostId()))
                .andExpect(jsonPath("$.data.lodge.name").value(lodgeDto.name()))
                .andExpect(jsonPath("$.data.lodge.description").value(lodgeDto.description()))
                .andExpect(jsonPath("$.data.images[0].id").value(imageDto.id()))
                .andExpect(jsonPath("$.data.images[0].title").value(imageDto.title()))
                .andExpect(jsonPath("$.data.rule.id").value(ruleDto.id()))
                .andExpect(jsonPath("$.data.rule.maxReservationDay").value(ruleDto.maxReservationDay()))
                .andExpect(jsonPath("$.data.rule.maxPersonnel").value(ruleDto.maxPersonnel()))
                .andExpect(jsonPath("$.data.rule.customization").value(ruleDto.customization()));

        verify(lodgeFacade, times(1)).readLodge(lodgeId);
    }
}
