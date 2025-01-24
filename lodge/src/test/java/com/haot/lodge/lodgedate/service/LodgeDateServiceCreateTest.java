package com.haot.lodge.lodgedate.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.implement.LodgeDateServiceImpl;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.repository.LodgeDateRepository;
import com.haot.lodge.util.TestEntityFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LodgeDateServiceCreateTest {

    @InjectMocks
    private LodgeDateServiceImpl lodgeDateService;

    @Mock
    private LodgeDateRepository lodgeDateRepository;

    @Test
    @DisplayName("시작 날짜가 현재 날짜 이전인 경우 오류")
    void create_startDateInPast_throwsException() {
        // given
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        // when & then
        LodgeException exception = assertThrows(LodgeException.class, () ->
                lodgeDateService.create(lodge, startDate, endDate, List.of())
        );
        assertEquals(ErrorCode.START_DATE_IN_PAST, exception.getErrorCode());
    }

    @Test
    @DisplayName("시작 날짜가 현재 날짜보다 너무 먼 경우 오류")
    void create_startDateTooFar_throwsException() {
        // given
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LocalDate startDate = LocalDate.now().plusYears(2);
        LocalDate endDate = startDate.plusDays(5);

        // when & then
        LodgeException exception = assertThrows(LodgeException.class, () ->
                lodgeDateService.create(lodge, startDate, endDate, List.of())
        );
        assertEquals(ErrorCode.START_DATE_TOO_FAR, exception.getErrorCode());
    }

    @Test
    @DisplayName("시작 날짜가 종료 날짜보다 뒤인 경우 오류")
    void create_startDateAfterEndDate_throwsException() {
        // given
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(5);

        // when & then
        LodgeException exception = assertThrows(LodgeException.class, () ->
                lodgeDateService.create(lodge, startDate, endDate, List.of())
        );
        assertEquals(ErrorCode.START_DATE_AFTER_END_DATE, exception.getErrorCode());
    }

    @Test
    @DisplayName("생성하려는 기간이 짧은 경우 (30일 미만) 오류")
    void createLodgeDate_dateRangeTooShort_throwsException() {
        // given
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(2);
        List<LocalDate> excludeDates = List.of();

        // when & then
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeDateService.create(lodge, startDate, endDate, excludeDates));

        assertEquals(ErrorCode.DATE_RANGE_TOO_SHORT, exception.getErrorCode());
    }

    @Test
    @DisplayName("생성하려는 기간이 긴 경우 (365일 초과) 오류")
    void createLodgeDate_dateRangeTooLong_throwsException() {
        // given
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusYears(2);
        List<LocalDate> excludeDates = List.of();

        // when & then
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeDateService.create(lodge, startDate, endDate, excludeDates));

        assertEquals(ErrorCode.DATE_RANGE_TOO_LONG, exception.getErrorCode());
    }

    @Test
    @DisplayName("해당 기간에 생성된 날짜가 이미 존재하는 경우 오류")
    void create_overlappingDates_throwsException() {
        // given
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        when(lodgeDateRepository.existsOverlappingDates(anyString(), eq(startDate), eq(endDate)))
                .thenReturn(true);

        // when & then
        LodgeException exception = assertThrows(LodgeException.class, () ->
                lodgeDateService.create(lodge, startDate, endDate, List.of())
        );
        assertEquals(ErrorCode.OVERLAPPING_DATES, exception.getErrorCode());
    }

}
