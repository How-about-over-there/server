package com.haot.point.application.dto.response;

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
) {}
