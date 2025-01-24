package com.haot.lodge.lodge.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeImageService;
import com.haot.lodge.application.service.LodgeRuleService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.util.TestEntityFixture;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LodgeFacadeDeleteTest {

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
    @DisplayName("숙소 삭제 성공")
    void deleteLodge_success() {
        // given
        String lodgeId = "validLodgeId";
        String userId = "HostUUID";
        Role userRole = Role.HOST;

        Lodge lodge = TestEntityFixture.createMockLodge(lodgeId);
        when(lodgeService.getValidLodgeById(lodgeId))
                .thenReturn(lodge);

        // when
        lodgeFacade.deleteLodge(userRole, userId, lodgeId);

        // then
        verify(lodgeService, times(1)).getValidLodgeById(lodgeId);
        verify(lodgeDateService, times(1)).deleteAllByLodge(lodge, userId);
        verify(lodgeImageService, times(1)).deleteAllByLodge(lodge, userId);
        verify(lodgeRuleService, times(1)).deleteByLodge(lodge, userId);
        verify(lodgeService, times(1)).delete(userId, lodge);
    }

    @Test
    @DisplayName("삭제 권한이 없는 경우")
    void deleteLodge_forbiddenAccess_throwsException() {
        // given
        String lodgeId = "validLodgeId";
        String userId = "unauthorizedUserId";
        Role userRole = Role.HOST;

        Lodge lodge = TestEntityFixture.createMockLodge(lodgeId);
        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);

        doThrow(new LodgeException(ErrorCode.FORBIDDEN_ACCESS_LODGE))
                .when(lodge).verifyProperty(userRole, userId);

        // when & then
        LodgeException exception = assertThrows(
                LodgeException.class, () -> lodgeFacade.deleteLodge(userRole, userId, lodgeId)
        );

        assertEquals(ErrorCode.FORBIDDEN_ACCESS_LODGE, exception.getErrorCode());
        verify(lodgeService, times(1)).getValidLodgeById(lodgeId);
        verifyNoInteractions(lodgeDateService, lodgeImageService, lodgeRuleService);
    }

    @Test
    @DisplayName("일치하는 숙소가 존재하지 않는 경우")
    void deleteLodge_lodgeNotFound_throwsException() {
        // given
        String lodgeId = "invalidLodgeId";
        String userId = "HostUUID";
        Role userRole = Role.HOST;

        when(lodgeService.getValidLodgeById(lodgeId))
                .thenThrow(new LodgeException(ErrorCode.LODGE_NOT_FOUND));

        // when & then
        LodgeException exception = assertThrows(
                LodgeException.class, () -> lodgeFacade.deleteLodge(userRole, userId, lodgeId)
        );

        assertEquals(ErrorCode.LODGE_NOT_FOUND, exception.getErrorCode());
        verify(lodgeService, times(1)).getValidLodgeById(lodgeId);
        verifyNoInteractions(lodgeDateService, lodgeImageService, lodgeRuleService);
    }

    @Test
    @DisplayName("삭제 불가능한 날짜가 존재하는 경우")
    void deleteLodge_cannotDeleteScheduledDate_throwsException() {
        // given
        String lodgeId = "validLodgeId";
        String userId = "HostUUID";
        Role userRole = Role.HOST;

        Lodge lodge = TestEntityFixture.createMockLodge(lodgeId);
        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);

        doThrow(new LodgeException(ErrorCode.CANNOT_DELETE_SCHEDULED_DATE))
                .when(lodgeDateService).deleteAllByLodge(lodge, userId);

        // when & then
        LodgeException exception = assertThrows(
                LodgeException.class,
                () -> lodgeFacade.deleteLodge(userRole, userId, lodgeId)
        );

        assertEquals(ErrorCode.CANNOT_DELETE_SCHEDULED_DATE, exception.getErrorCode());
        verify(lodgeService, times(1)).getValidLodgeById(lodgeId);
        verify(lodgeDateService, times(1)).deleteAllByLodge(lodge, userId);
        verifyNoInteractions(lodgeImageService, lodgeRuleService);
    }

}
