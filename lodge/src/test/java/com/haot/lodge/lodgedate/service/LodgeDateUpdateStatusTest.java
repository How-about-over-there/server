package com.haot.lodge.lodgedate.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.haot.lodge.application.facade.LodgeDateFacade;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.common.utils.RedisLockManager;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;

@ExtendWith(MockitoExtension.class)
public class LodgeDateUpdateStatusTest {

    @Mock
    private RedisLockManager redisLockManager;

    @Mock
    private LodgeDateService lodgeDateService;

    @InjectMocks
    private LodgeDateFacade lodgeDateFacade;


    @Test
    public void testUpdateStatus_SequentialProcessingWithError() throws InterruptedException {
        // Given: Request
        List<String> dateIds = List.of("UUID1", "UUID2", "UUID3");
        String requestStatus = "WAITING";

        // redisLockManager.acquireLocks 가 호출되었을 때 Mock 락 객체를 반환
        RLock mockLock = mock(RLock.class);
        when(redisLockManager
                .acquireLocks(eq(dateIds), anyString(), anyLong(), anyLong()))
                .thenReturn(List.of(mockLock));

        // lodgeDateService.updateStatusOf 첫 번째 호출에서는 아무 동작도 하지 않고 (doNothing())
        // 이후 호출에서 LodgeException 을 던지도록 정의 (doThrow)
        doNothing()
                .doThrow(new LodgeException(ErrorCode.ALREADY_CHANGED_DATE_STATUS))
                .when(lodgeDateService)
                .updateStatusOf(eq(dateIds), eq(ReservationStatus.WAITING));

        // 성공과 실패 결과를 추적하기 위해 사용
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        // When:
        // ExecutorService 를 사용해 5개의 쓰레드가 동시에 lodgeDateFacade.updateStatus 를 호출
        // 첫 번째 쓰레드는 정상적으로 상태를 변경하고, 이후 쓰레드는 이미 상태가 변경되었기 때문에 LodgeException 이 발생
        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                try {
                    lodgeDateFacade.updateStatus(dateIds, requestStatus);
                    successCount.incrementAndGet();
                } catch (LodgeException e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 쓰레드 끝날때까지 기다리기
        latch.await();

        // Then: 결과 확인
        assertEquals(1, successCount.get(), "하나의 쓰레드만 성공");
        assertEquals(4, failureCount.get(), "다른 쓰레드는 요청 실패");

        //redisLockManager.acquireLocks 와 releaseLocks 가 5번 호출되었는지 확인.
        //lodgeDateService.updateStatusOf가 총 5번 호출되었는지 확인.
        verify(redisLockManager, times(5)).acquireLocks(eq(dateIds), anyString(), anyLong(), anyLong());
        verify(redisLockManager, times(5)).releaseLocks(anyList());
        verify(lodgeDateService, times(5)).updateStatusOf(eq(dateIds), eq(ReservationStatus.WAITING));

        executorService.shutdown();
    }
}
