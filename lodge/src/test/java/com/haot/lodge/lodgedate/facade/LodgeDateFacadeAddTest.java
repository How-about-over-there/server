package com.haot.lodge.lodgedate.facade;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.facade.LodgeDateFacade;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateAddRequest;
import com.haot.lodge.util.TestEntityFixture;
import com.haot.submodule.role.Role;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LodgeDateFacadeAddTest {

    @InjectMocks
    private LodgeDateFacade lodgeDateFacade;

    @Mock
    private LodgeService lodgeService;

    @Mock
    private LodgeDateService lodgeDateService;

    @Test
    @DisplayName("숙소 날짜 추가 성공")
    void addLodgeDate_success() {
        // given
        String userId = "UserUUID";
        Role userRole = Role.HOST;
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LodgeDateAddRequest request = new LodgeDateAddRequest(
                lodge.getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(7),
                List.of()
        );

        when(lodgeService.getValidLodgeById(request.lodgeId())).thenReturn(lodge);

        // when
        assertDoesNotThrow(() -> lodgeDateFacade.addLodgeDate(userRole, userId, request));

        // then
        verify(lodgeService).getValidLodgeById(request.lodgeId());
        verify(lodgeDateService).create(
                lodge, request.startDate(), request.endDate(), request.excludeDates()
        );
    }

    @Test
    @DisplayName("숙소 아이디와 일치하는 숙소가 없는 경우 오류")
    void addLodgeDate_lodgeNotFound_throwsException() {
        // given
        String userId = "UserUUID";
        Role userRole = Role.HOST;
        LodgeDateAddRequest request = new LodgeDateAddRequest(
                "invalidLodgeId",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(7),
                List.of()
        );

        when(lodgeService.getValidLodgeById(request.lodgeId()))
                .thenThrow(new LodgeException(ErrorCode.LODGE_NOT_FOUND));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeDateFacade.addLodgeDate(userRole, userId, request));

        // then
        assertEquals(ErrorCode.LODGE_NOT_FOUND, exception.getErrorCode());
        verify(lodgeService).getValidLodgeById(request.lodgeId());
        verifyNoInteractions(lodgeDateService); // 날짜 생성 서비스 호출 안됨
    }

    @Test
    @DisplayName("로그인한 Host 유저가 해당 숙소의 관리자가 아닌 경우 오류")
    void addLodgeDate_forbiddenAccess_throwsException() {
        // given
        String userId = "unauthorizedUserId";
        Role userRole = Role.HOST;
        Lodge lodge = TestEntityFixture.createMockLodge("LodgeUUID");
        LodgeDateAddRequest request = new LodgeDateAddRequest(
                lodge.getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(7),
                List.of()
        );

        when(lodgeService.getValidLodgeById(request.lodgeId())).thenReturn(lodge);
        doThrow(new LodgeException(ErrorCode.FORBIDDEN_ACCESS_LODGE))
                .when(lodge)
                .verifyProperty(userRole, userId);

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeDateFacade.addLodgeDate(userRole, userId, request));

        // then
        assertEquals(ErrorCode.FORBIDDEN_ACCESS_LODGE, exception.getErrorCode());
        verify(lodgeService).getValidLodgeById(request.lodgeId());
        verifyNoInteractions(lodgeDateService);
    }


}
