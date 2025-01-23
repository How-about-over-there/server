package com.haot.lodge.lodge.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.presentation.request.lodge.LodgeUpdateRequest;
import com.haot.lodge.util.TestEntityFixture;
import com.haot.submodule.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LodgeFacadeUpdateTest {
    @InjectMocks
    private LodgeFacade lodgeFacade;

    @Mock
    private LodgeService lodgeService;

    @Test
    @DisplayName("숙소 정보 수정 성공")
    void updateLodge_success() {
        // given
        Role userRole = Role.HOST;
        String userId = "HostUUID";
        String lodgeId = "LodgeUUID";

        Lodge lodge = TestEntityFixture.createMockLodge(lodgeId);
        doNothing()
                .when(lodge)
                .verifyProperty(userRole, userId);

        LodgeUpdateRequest request = getUpdateRequest();

        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);
        doNothing().when(lodgeService).update(
                lodge,
                request.name(),
                request.description(),
                request.address(),
                request.term(),
                request.basicPrice()
        );

        // when
        lodgeFacade.updateLodge(userRole, userId, lodgeId, request);

        // then
        verify(lodgeService, times(1)).getValidLodgeById(lodgeId);
        verify(lodge).verifyProperty(userRole, userId);
        verify(lodgeService, times(1)).update(
                lodge,
                request.name(),
                request.description(),
                request.address(),
                request.term(),
                request.basicPrice()
        );
    }

    @Test
    @DisplayName("숙소 아이디와 일치하는 숙소가 존재하지 않는 경우")
    void updateLodge_shouldThrowLodgeNotFound_whenLodgeDoesNotExist() {
        // given
        Role userRole = Role.HOST;
        String userId = "HostUUID";
        String lodgeId = "invalidLodgeId";

        LodgeUpdateRequest request = getUpdateRequest();

        when(lodgeService.getValidLodgeById(lodgeId))
                .thenThrow(new LodgeException(ErrorCode.LODGE_NOT_FOUND));

        // when & then
        LodgeException exception = assertThrows(
                LodgeException.class,
                () -> lodgeFacade.updateLodge(userRole, userId, lodgeId, request)
        );

        assertEquals(ErrorCode.LODGE_NOT_FOUND, exception.getErrorCode());
        verify(lodgeService, times(1)).getValidLodgeById(lodgeId);
        verifyNoMoreInteractions(lodgeService);
    }

    @Test
    @DisplayName("수정 권한이 없는 경우")
    void updateLodge_shouldThrowForbiddenAccessLodge_whenUserNotOwner() {
        // given
        Role userRole = Role.HOST;
        String userId = "unauthorizedUserId";
        String lodgeId = "LodgeUUID";

        Lodge lodge = TestEntityFixture.createMockLodge(lodgeId);
        doThrow(new LodgeException(ErrorCode.FORBIDDEN_ACCESS_LODGE))
                .when(lodge)
                .verifyProperty(userRole, userId);

        LodgeUpdateRequest request = getUpdateRequest();

        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);

        // when & then
        LodgeException exception = assertThrows(
                LodgeException.class,
                () -> lodgeFacade.updateLodge(userRole, userId, lodgeId, request)
        );

        assertEquals(ErrorCode.FORBIDDEN_ACCESS_LODGE, exception.getErrorCode());
        verify(lodgeService, times(1)).getValidLodgeById(lodgeId);
        verify(lodge, times(1)).verifyProperty(userRole, userId);
        verifyNoMoreInteractions(lodgeService);
    }


    private LodgeUpdateRequest getUpdateRequest() {
        return new LodgeUpdateRequest(
                "리뉴얼된 숙소",
                "더 좋아졌어요",
                "Updated Address",
                10,
                200.0
        );
    }
}
