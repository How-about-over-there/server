package com.haot.lodge.lodgedate.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.dto.LodgeDateDto;
import com.haot.lodge.application.dto.LodgeDateSearchCriteria;
import com.haot.lodge.application.facade.LodgeDateFacade;
import com.haot.lodge.application.response.LodgeDateReadResponse;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateSearchParams;
import com.haot.lodge.util.TestEntityFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class LodgeDateFacadeReadTest {

    @InjectMocks
    private LodgeDateFacade lodgeDateFacade;

    @Mock
    private LodgeService lodgeService;

    @Mock
    private LodgeDateService lodgeDateService;

    @Test
    @DisplayName("숙소 날짜들 조회 성공")
    void readLodgeDates_success() {
        // given
        String lodgeId = "lodge123";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("date").ascending());
        LodgeDateSearchParams params = getLodgeDateSearchParams(lodgeId);
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        Slice<LodgeDateDto> mockDates = getMockLodgeDateSlice().map(LodgeDateDto::from);

        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);
        when(lodgeDateService.readAllBy(
                eq(pageable),
                eq(LodgeDateSearchCriteria.of(lodge, params))
        )).thenReturn(mockDates);

        // when
        Slice<LodgeDateReadResponse> result = lodgeDateFacade.readLodgeDates(pageable, params);

        // then
        assertNotNull(result);
        assertEquals(mockDates.getSize(), result.getSize());
        assertEquals(mockDates.getContent().size(), result.getContent().size());
        verify(lodgeService).getValidLodgeById(lodgeId);
        verify(lodgeDateService).readAllBy(eq(pageable), eq(LodgeDateSearchCriteria.of(lodge, params)));
    }

    @Test
    @DisplayName("숙소 아이디와 일치하는 숙소가 없는 경우 오류")
    void readLodgeDates_lodgeNotFound_throwsException() {
        // given
        String lodgeId = "invalidLodgeId";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("date").ascending());
        LodgeDateSearchParams params = getLodgeDateSearchParams(lodgeId);

        when(lodgeService.getValidLodgeById(lodgeId))
                .thenThrow(new LodgeException(ErrorCode.LODGE_NOT_FOUND));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeDateFacade.readLodgeDates(pageable, params));

        // then
        assertEquals(ErrorCode.LODGE_NOT_FOUND, exception.getErrorCode());
        verify(lodgeService).getValidLodgeById(lodgeId);
        verifyNoInteractions(lodgeDateService);
    }

    @Test
    @DisplayName("지원하지 않는 정렬 기준으로 요청한 경우 오류")
    void readLodgeDates_unsupportedSortType_throwsException() {
        // given
        String lodgeId = "lodge123";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("unsupportedField").ascending());
        LodgeDateSearchParams params = getLodgeDateSearchParams(lodgeId);
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");

        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);

        doThrow(new LodgeException(ErrorCode.UNSUPPORTED_SORT_TYPE))
                .when(lodgeDateService)
                .readAllBy(eq(pageable), any(LodgeDateSearchCriteria.class));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeDateFacade.readLodgeDates(pageable, params));

        // then
        assertEquals(ErrorCode.UNSUPPORTED_SORT_TYPE, exception.getErrorCode());
        verify(lodgeService).getValidLodgeById(lodgeId);
        verify(lodgeDateService).readAllBy(eq(pageable), any(LodgeDateSearchCriteria.class));
    }

    private LodgeDateSearchParams getLodgeDateSearchParams(String lodgeId) {
        return new LodgeDateSearchParams(
                lodgeId,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
    }

    public Slice<LodgeDate> getMockLodgeDateSlice() {
        LodgeDate lodgeDate1 = getMockLodgeDate("date1", LocalDate.now(), 100.0, ReservationStatus.WAITING);
        LodgeDate lodgeDate2 = getMockLodgeDate("date2", LocalDate.now().plusDays(1), 120.0, ReservationStatus.COMPLETE);
        LodgeDate lodgeDate3 = getMockLodgeDate("date3", LocalDate.now().plusDays(2), 110.0, ReservationStatus.WAITING);

        List<LodgeDate> lodgeDates = List.of(lodgeDate1, lodgeDate2, lodgeDate3);
        return new SliceImpl<>(lodgeDates);
    }

    private LodgeDate getMockLodgeDate(String id, LocalDate date, Double price, ReservationStatus status) {
        LodgeDate lodgeDate = mock(LodgeDate.class);
        when(lodgeDate.getId()).thenReturn(id);
        when(lodgeDate.getDate()).thenReturn(date);
        when(lodgeDate.getPrice()).thenReturn(price);
        when(lodgeDate.getStatus()).thenReturn(status);
        return lodgeDate;
    }

}
