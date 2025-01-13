package com.haot.reservation.infrastructure.dtos.point;

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
) {}
