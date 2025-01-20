package com.haot.lodge.lodgedate.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.haot.lodge.application.dto.LodgeDateDto;
import com.haot.lodge.application.facade.LodgeDateFacade;
import com.haot.lodge.application.response.LodgeDateReadResponse;
import com.haot.lodge.common.response.SliceResponse;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.presentation.controller.LodgeDateController;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateSearchParams;
import java.time.LocalDate;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LodgeDateController.class)
@ExtendWith(MockitoExtension.class)
public class LodgeDateControllerReadAllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LodgeDateFacade lodgeDateFacade;

    @Test
    @DisplayName("숙소 날짜 목록 조회 성공 테스트")
    void readAllLodgeDates_Success() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 30, Sort.by(Sort.Order.asc("date")));

        LodgeDateSearchParams searchParams = new LodgeDateSearchParams(
                "1",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        );

        LodgeDateDto lodgeDateDto = new LodgeDateDto(
                "1",
                LocalDate.of(2025, 1, 20),
                150.0,
                ReservationStatus.WAITING
        );

        LodgeDateReadResponse responseItem = new LodgeDateReadResponse(lodgeDateDto);

        Slice<LodgeDateReadResponse> mockSlice = new SliceImpl<>(
                List.of(responseItem),
                pageable,
                false
        );

        SliceResponse<LodgeDateReadResponse> expectedResponse = SliceResponse.of(mockSlice);

        // When
        when(lodgeDateFacade.readLodgeDates(eq(pageable), eq(searchParams)))
                .thenReturn(mockSlice);

        // Then
        mockMvc.perform(get("/api/v1/lodge-dates")
                        .param("lodgeId", searchParams.lodgeId())
                        .param("start", searchParams.start().toString())
                        .param("end", searchParams.end().toString())
                        .param("page", "0")
                        .param("size", "30")
                        .param("sort", "date,ASC")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("5000"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("API 요청에 성공했습니다"))
                .andExpect(jsonPath("$.data.hasNext").value(false))
                .andExpect(jsonPath("$.data.sliceNumber").value(0))
                .andExpect(jsonPath("$.data.numberOfElements").value(1))
                .andExpect(jsonPath("$.data.content[0].date.id").value(lodgeDateDto.id()))
                .andExpect(jsonPath("$.data.content[0].date.date").value(lodgeDateDto.date().toString()))
                .andExpect(jsonPath("$.data.content[0].date.price").value(lodgeDateDto.price()))
                .andExpect(jsonPath("$.data.content[0].date.status").value(lodgeDateDto.status().toString()));

        verify(lodgeDateFacade, times(1)).readLodgeDates(eq(pageable), eq(searchParams));
    }

}
