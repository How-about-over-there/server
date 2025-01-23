package com.haot.lodge.lodge.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.dto.LodgeSearchCriteria;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.response.LodgeReadAllResponse;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.presentation.request.lodge.LodgeSearchParams;
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
public class LodgeFacadeReadAllTest {

    @InjectMocks
    private LodgeFacade lodgeFacade;

    @Mock
    private LodgeService lodgeService;

    @Test
    @DisplayName("숙소 목록 조회 성공")
    void readAllLodgeBy_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        LodgeSearchParams searchParams = new LodgeSearchParams(
                "HostUUID",
                "해변가 숙소",
                "City Address",
                7,
                4,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 5)
        );

        List<Lodge> lodges = List.of(TestEntityFixture.createMockLodge("UUID"));
        Slice<Lodge> lodgeSlice = new SliceImpl<>(lodges, pageable, false);

        when(lodgeService.readAllBy(
                any(Pageable.class),
                any(LodgeSearchCriteria.class)
        )).thenReturn(lodgeSlice);

        // when
        Slice<LodgeReadAllResponse> result = lodgeFacade.readAllLodgeBy(pageable, searchParams);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        LodgeReadAllResponse response = result.getContent().get(0);
        assertEquals("UUID", response.lodge().id());
        assertEquals("해변가 숙소", response.lodge().name());
        assertEquals(1, response.images().size());

        verify(lodgeService, times(1)).readAllBy(any(Pageable.class), any(LodgeSearchCriteria.class));
    }

    @Test
    @DisplayName("지원하지 않는 정렬 기준으로 요청한 경우")
    void readAllLodgeBy_unsupportedSortType_throwsException() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("unsupported").ascending());
        LodgeSearchParams searchParams = new LodgeSearchParams(
                "HostUUID",
                "해변가 숙소",
                "City Address",
                7,
                4,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 5)
        );

        when(lodgeService.readAllBy(
                any(Pageable.class),
                any(LodgeSearchCriteria.class)
        )).thenThrow(new LodgeException(ErrorCode.UNSUPPORTED_SORT_TYPE));

        // when & then
        LodgeException exception = assertThrows(
                LodgeException.class,
                () -> lodgeFacade.readAllLodgeBy(pageable, searchParams)
        );

        assertEquals(ErrorCode.UNSUPPORTED_SORT_TYPE, exception.getErrorCode());
        verify(lodgeService, times(1)).readAllBy(any(Pageable.class), any(LodgeSearchCriteria.class));
    }
}
