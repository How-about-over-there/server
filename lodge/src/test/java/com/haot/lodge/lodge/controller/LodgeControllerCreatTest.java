package com.haot.lodge.lodge.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.response.LodgeCreateResponse;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.presentation.controller.LodgeController;
import com.haot.lodge.presentation.request.lodge.LodgeCreateRequest;
import com.haot.submodule.role.Role;
import java.time.LocalDate;
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
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(LodgeController.class)
@ExtendWith(MockitoExtension.class)
public class LodgeControllerCreatTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LodgeFacade lodgeFacade;

    @Test
    @DisplayName("숙소 생성 성공 테스트")
    void createLodge_Success() throws Exception {
        // Given
        String userId = "UserUUID";
        Role role = Role.HOST;

        LodgeCreateRequest request = new LodgeCreateRequest(
                "해변가 숙소",
                "아름다운 해변가 숙소입니다.",
                "123 Ocean View Drive",
                7,
                250.00,
                mock(MultipartFile.class),
                "숙소 사진",
                "밖에서 볼 수 있는 숙소 사진입니다.",
                10,
                5,
                "애완 동물 출입 금지",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                List.of(LocalDate.of(2025, 5, 1),
                        LocalDate.of(2025, 12, 25)
                )
        );

        LodgeDto lodgeDto = TestUtil.createMockLodgeDto();

        ApiResponse<LodgeCreateResponse> expectedResponse = ApiResponse.success(new LodgeCreateResponse(lodgeDto));

        // When
        when(lodgeFacade
                .createLodge(eq(userId), any(LodgeCreateRequest.class)))
                .thenReturn(lodgeDto);

        // Then
        mockMvc.perform(multipart("/api/v1/lodges")
                        .file("image", request.image().getBytes())
                        .param("name", request.name())
                        .param("description", request.description())
                        .param("address", request.address())
                        .param("term", String.valueOf(request.term()))
                        .param("basicPrice", String.valueOf(request.basicPrice()))
                        .param("imageTitle", request.imageTitle())
                        .param("imageDescription", request.imageDescription())
                        .param("maxReservationDay", String.valueOf(request.maxReservationDay()))
                        .param("maxPersonnel", String.valueOf(request.maxPersonnel()))
                        .param("customization", request.customization())
                        .param("startDate", request.startDate().toString())
                        .param("endDate", request.endDate().toString())
                        .param("excludeDates", "2025-05-01", "2025-12-25")
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("5000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("API 요청에 성공했습니다"))
                .andExpect(jsonPath("$.data.lodge.id").value(lodgeDto.id()))
                .andExpect(jsonPath("$.data.lodge.hostId").value(lodgeDto.hostId()))
                .andExpect(jsonPath("$.data.lodge.name").value(lodgeDto.name()))
                .andExpect(jsonPath("$.data.lodge.description").value(lodgeDto.description()))
                .andExpect(jsonPath("$.data.lodge.address").value(lodgeDto.address()))
                .andExpect(jsonPath("$.data.lodge.term").value(lodgeDto.term()))
                .andExpect(jsonPath("$.data.lodge.basicPrice").value(lodgeDto.basicPrice()));

        verify(lodgeFacade, times(1)).createLodge(eq(userId), any(LodgeCreateRequest.class));
    }
}
