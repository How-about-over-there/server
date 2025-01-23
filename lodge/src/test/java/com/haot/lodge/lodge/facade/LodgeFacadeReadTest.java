package com.haot.lodge.lodge.facade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.application.response.LodgeReadOneResponse;
import com.haot.lodge.application.service.LodgeRuleService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeRule;
import com.haot.lodge.util.TestEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LodgeFacadeReadTest {

    @InjectMocks
    private LodgeFacade lodgeFacade;

    @Mock
    private LodgeService lodgeService;

    @Mock
    private LodgeRuleService lodgeRuleService;


    @Test
    @DisplayName("숙소 조회 성공")
    void readLodge_success() {
        // given
        String lodgeId = "LodgeUUID";
        Lodge lodge = TestEntityFixture.createMockLodge(lodgeId);
        LodgeRule lodgeRule = TestEntityFixture.createMockLodgeRule();
        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);
        when(lodgeRuleService.getLodgeRuleByLodgeId(lodgeId)).thenReturn(lodgeRule);

        // when
        LodgeReadOneResponse response = lodgeFacade.readLodge(lodgeId);

        // then
        assertNotNull(response);
        assertEquals(lodge.getId(), response.lodge().id());
        assertEquals(1, response.images().size());
        assertEquals(lodgeRule.getId(), response.rule().id());
        verify(lodgeService).getValidLodgeById(lodgeId);
        verify(lodgeRuleService).getLodgeRuleByLodgeId(lodgeId);
    }

    @Test
    @DisplayName("일치하는 숙소가 없는 경우")
    void readLodge_lodgeNotFound() {
        // given
        String lodgeId = "invalidLodgeId";
        when(lodgeService.getValidLodgeById(lodgeId))
                .thenThrow(new LodgeException(ErrorCode.LODGE_NOT_FOUND));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.readLodge(lodgeId));

        // then
        assertEquals(ErrorCode.LODGE_NOT_FOUND, exception.getErrorCode());
        verify(lodgeService).getValidLodgeById(lodgeId);
        verifyNoInteractions(lodgeRuleService); // lodge 조회 실패 시 ruleService 는 호출되지 않는다
    }

    @Test
    @DisplayName("숙소와 연결된 규칙을 찾지 못했을 경우")
    void readLodge_lodgeRuleNotFound() {
        // given
        String lodgeId = "LodgeUUID";
        Lodge lodge = TestEntityFixture.createMockLodge(lodgeId);
        when(lodgeService.getValidLodgeById(lodgeId)).thenReturn(lodge);
        when(lodgeRuleService.getLodgeRuleByLodgeId(lodgeId))
                .thenThrow(new LodgeException(ErrorCode.LODGE_RULE_NOT_FOUND));

        // when
        LodgeException exception = assertThrows(LodgeException.class,
                () -> lodgeFacade.readLodge(lodgeId));

        // then
        assertEquals(ErrorCode.LODGE_RULE_NOT_FOUND, exception.getErrorCode());
        verify(lodgeService).getValidLodgeById(lodgeId);
        verify(lodgeRuleService).getLodgeRuleByLodgeId(lodgeId);
    }

}
