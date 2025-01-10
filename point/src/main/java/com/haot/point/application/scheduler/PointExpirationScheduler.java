package com.haot.point.application.scheduler;

import com.haot.point.application.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PointExpirationScheduler {

    private final PointService pointService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void runDailyExpireBatch() {
        LocalDateTime targetDate = LocalDateTime.now().minusDays(1); // 전날 기준
        int page = 0;
        int batchSize = 100;

        while (pointService.expirePoints(targetDate, page, batchSize)) {
            page++;
        }
    }
}
