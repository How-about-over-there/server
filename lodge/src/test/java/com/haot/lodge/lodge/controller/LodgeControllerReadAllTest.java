package com.haot.lodge.lodge.controller;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeImageDto;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.response.LodgeReadAllResponse;
import com.haot.lodge.common.response.SliceResponse;
import com.haot.lodge.presentation.controller.LodgeController;
import com.haot.lodge.presentation.request.lodge.LodgeSearchParams;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LodgeController.class)
@ExtendWith(MockitoExtension.class)
public class LodgeControllerReadAllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LodgeFacade lodgeFacade;

    @Test
    @DisplayName("숙소 목록 조회 성공 테스트")
    void readAllLodges_Success() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("updatedAt")
        ));

        LodgeSearchParams searchParams = new LodgeSearchParams(
                "UserUUID",
                "해변가",
                "Ocean",
                10,
                5,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 7)
        );

        LodgeDto lodgeDto = TestUtil.createMockLodgeDto();

        LodgeImageDto imageDto = TestUtil.createMockLodgeImageDto();

        LodgeReadAllResponse responseItem = new LodgeReadAllResponse(lodgeDto, List.of(imageDto));

        Slice<LodgeReadAllResponse> mockSlice = new SliceImpl<>(
                List.of(responseItem),
                pageable,
                false
        );

        SliceResponse<LodgeReadAllResponse> expectedResponse = SliceResponse.of(mockSlice);

        // When
        when(lodgeFacade.readAllLodgeBy(eq(pageable), eq(searchParams)))
                .thenReturn(mockSlice);

        // Then
        mockMvc.perform(get("/api/v1/lodges")
                        .param("hostId", searchParams.hostId())
                        .param("name", searchParams.name())
                        .param("address", searchParams.address())
                        .param("maxReservationDay", String.valueOf(searchParams.maxReservationDay()))
                        .param("maxPersonnel", String.valueOf(searchParams.maxPersonnel()))
                        .param("checkInDate", searchParams.checkInDate().toString())
                        .param("checkOutDate", searchParams.checkOutDate().toString())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,DESC", "updatedAt,DESC")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("5000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("API 요청에 성공했습니다"))
                .andExpect(jsonPath("$.data.hasNext").value(false))
                .andExpect(jsonPath("$.data.sliceNumber").value(0))
                .andExpect(jsonPath("$.data.numberOfElements").value(1))
                .andExpect(jsonPath("$.data.content[0].lodge.id").value(lodgeDto.id()))
                .andExpect(jsonPath("$.data.content[0].lodge.hostId").value(lodgeDto.hostId()))
                .andExpect(jsonPath("$.data.content[0].lodge.name").value(lodgeDto.name()))
                .andExpect(jsonPath("$.data.content[0].images[0].id").value(imageDto.id()))
                .andExpect(jsonPath("$.data.content[0].images[0].title").value(imageDto.title()));

        verify(lodgeFacade, times(1)).readAllLodgeBy(eq(pageable), eq(searchParams));
    }
}
