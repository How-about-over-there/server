package com.haot.lodge.lodge.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeImageService;
import com.haot.lodge.application.service.LodgeRuleService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.presentation.request.lodge.LodgeCreateRequest;
import com.haot.lodge.util.TestEntityFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class LodgeFacadeCreateTest {
    @InjectMocks
    private LodgeFacade lodgeFacade;

    @Mock
    private LodgeService lodgeService;

    @Mock
    private LodgeImageService lodgeImageService;

    @Mock
    private LodgeRuleService lodgeRuleService;

    @Mock
    private LodgeDateService lodgeDateService;

    @Test
    @DisplayName("숙소 생성 성공 테스트")
    void createLodge_success() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createValidMockLodgeCreateRequest();
        Lodge lodge = TestEntityFixture.createMockLodge("UUID");
        when(lodgeService.create(
                userId, request.name(), request.description(), request.address(), request.term(), request.basicPrice())
        ).thenReturn(lodge);

        // when
        LodgeDto lodgeDto = lodgeFacade.createLodge(userId, request);

        // then
        assertNotNull(lodgeDto);
        assertEquals(lodge.getName(), lodgeDto.name());
        verify(lodgeImageService).create(lodge, request.image(), request.imageTitle(), request.imageDescription());
        verify(lodgeRuleService).create(lodge, request.maxReservationDay(), request.maxPersonnel(), request.customization());
        verify(lodgeDateService).create(lodge, request.startDate(), request.endDate(), request.excludeDates());
    }

    @Test
    @DisplayName("해당 유저가 같은 이름의 숙소를 이미 만든 경우")
    void createLodge_alreadyExistLodgeName() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createValidMockLodgeCreateRequest();
        when(lodgeService.create(
                userId, request.name(), request.description(),
                request.address(), request.term(), request.basicPrice())
        ).thenThrow(new LodgeException(ErrorCode.ALREADY_EXIST_LODGE_NAME));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.createLodge(userId, request));

        // then
        assertEquals(ErrorCode.ALREADY_EXIST_LODGE_NAME, exception.getErrorCode());
    }

    @Test
    @DisplayName("시작 날짜가 현재 날짜 이전인 경우 실패")
    void createLodge_startDateInPast_throwsException() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createInvalidDateMockLodgeCreateRequest(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(7)
        );

        doThrow(new LodgeException(ErrorCode.START_DATE_IN_PAST))
                .when(lodgeDateService)
                .create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.createLodge(userId, request));

        // then
        assertEquals(ErrorCode.START_DATE_IN_PAST, exception.getErrorCode());
        verify(lodgeDateService).create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));
    }

    @Test
    @DisplayName("시작 날짜가 현재 날짜보다 1년 이상 차이가 날 경우")
    void createLodge_startDateTooFar_throwsException() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createInvalidDateMockLodgeCreateRequest(
                LocalDate.now().plusYears(2),
                LocalDate.now().plusYears(2).plusDays(10)
        );

        doThrow(new LodgeException(ErrorCode.START_DATE_TOO_FAR))
                .when(lodgeDateService)
                .create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.createLodge(userId, request));

        // then
        assertEquals(ErrorCode.START_DATE_TOO_FAR, exception.getErrorCode());
        verify(lodgeDateService).create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));
    }

    @Test
    @DisplayName("시작 날짜가 종료 날짜보다 뒤인 경우")
    void createLodge_startDateAfterEndDate_throwsException() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createInvalidDateMockLodgeCreateRequest(
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(5)
        );

        doThrow(new LodgeException(ErrorCode.START_DATE_AFTER_END_DATE))
                .when(lodgeDateService)
                .create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.createLodge(userId, request));

        // then
        assertEquals(ErrorCode.START_DATE_AFTER_END_DATE, exception.getErrorCode());
        verify(lodgeDateService).create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));
    }

    @Test
    @DisplayName("생성하려는 기간이 너무 짧은 경우")
    void createLodge_dateRangeTooShort_throwsException() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createInvalidDateMockLodgeCreateRequest(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1)
        );

        doThrow(new LodgeException(ErrorCode.DATE_RANGE_TOO_SHORT))
                .when(lodgeDateService)
                .create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.createLodge(userId, request));

        // then
        assertEquals(ErrorCode.DATE_RANGE_TOO_SHORT, exception.getErrorCode());
        verify(lodgeDateService).create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));
    }

    @Test
    @DisplayName("생성하려는 기간이 너무 긴 경우")
    void createLodge_dateRangeTooLong_throwsException() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createInvalidDateMockLodgeCreateRequest(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(400)
        );

        doThrow(new LodgeException(ErrorCode.DATE_RANGE_TOO_LONG))
                .when(lodgeDateService)
                .create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.createLodge(userId, request));

        // then
        assertEquals(ErrorCode.DATE_RANGE_TOO_LONG, exception.getErrorCode());
        verify(lodgeDateService).create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));
    }

    @Test
    @DisplayName("해당 기간에 생성된 날짜가 이미 있는 경우")
    void createLodge_overlappingDates_throwsException() {
        // given
        String userId = "UserUUID";
        LodgeCreateRequest request = createValidMockLodgeCreateRequest();

        doThrow(new LodgeException(ErrorCode.OVERLAPPING_DATES))
                .when(lodgeDateService)
                .create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.createLodge(userId, request));

        // then
        assertEquals(ErrorCode.OVERLAPPING_DATES, exception.getErrorCode());
        verify(lodgeDateService).create(any(), eq(request.startDate()), eq(request.endDate()), eq(request.excludeDates()));
    }

    private LodgeCreateRequest createValidMockLodgeCreateRequest() {
        return new LodgeCreateRequest(
                "해변가 숙소",
                "아름다운 해변가 숙소입니다.",
                "123 Street, City",
                7,
                2500.0,
                mock(MultipartFile.class),
                "숙소 사진",
                "밖에서 볼 수 있는 숙소 사진입니다.",
                10,
                5,
                "애완견 출입 금지입니다.",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(31),
                List.of()
        );
    }

    private LodgeCreateRequest createInvalidDateMockLodgeCreateRequest(LocalDate startDate, LocalDate endDate) {
        return new LodgeCreateRequest(
                "해변가 숙소",
                "아름다운 해변가 숙소입니다.",
                "123 Street, City",
                7,
                2500.0,
                mock(MultipartFile.class),
                "숙소 사진",
                "밖에서 볼 수 있는 숙소 사진입니다.",
                10,
                5,
                "애완견 출입 금지입니다.",
                startDate,
                endDate,
                List.of()
        );
    }

}
