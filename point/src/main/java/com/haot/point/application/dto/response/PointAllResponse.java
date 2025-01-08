package com.haot.point.application.dto.response;

import com.haot.point.domain.model.Point;
import com.haot.point.domain.model.PointHistory;

import java.time.LocalDateTime;

public record PointAllResponse(
        String pointId,
        String historyId,
        String userId,
        Double points,
        Double totalPoints,
        String type,
        String description,
        LocalDateTime expiredAt,
        String status
) {
    public static PointAllResponse of(Point point, PointHistory pointHistory) {
        return new PointAllResponse(
                point.getId(),
                pointHistory.getId(),
                point.getUserId(),
                pointHistory.getPoints(),
                point.getTotalPoints(),
                pointHistory.getType().name(),
                pointHistory.getDescription(),
                pointHistory.getExpiredAt(),
                pointHistory.getStatus().name()
        );
    }
}
