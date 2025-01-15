package com.haot.point.application.dto.response;

import com.haot.point.domain.model.PointHistory;

import java.time.LocalDateTime;

public record PointHistoryResponse(
        String historyId,
        String pointId,
        String userId,
        Double points,
        String type,
        String description,
        LocalDateTime expiredAt,
        String status
) {
    public static PointHistoryResponse of(PointHistory pointHistory) {
        return new PointHistoryResponse(
                pointHistory.getId(),
                pointHistory.getPoint().getId(),
                pointHistory.getUserId(),
                pointHistory.getPoints(),
                pointHistory.getType().name(),
                pointHistory.getDescription(),
                pointHistory.getExpiredAt(),
                pointHistory.getStatus().name()
        );
    }
}
