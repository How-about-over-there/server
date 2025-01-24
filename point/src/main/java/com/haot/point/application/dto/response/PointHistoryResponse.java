package com.haot.point.application.dto.response;

import com.haot.point.domain.model.PointHistory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "포인트 내역 RESPONSE DTO")
public record PointHistoryResponse(
        String historyId,
        String pointId,
        String userId,
        Double points,
        String type,
        String description,
        LocalDateTime expiredAt,
        String status
) implements Serializable {
    private static final long serialVersionUID = 1L;

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
