package com.haot.point.application.dto.response;

import com.haot.point.domain.model.Point;

public record PointResponse(
        String pointId,
        String userId,
        Double totalPoints
) {
    public static PointResponse of(Point point) {
        return new PointResponse(
                point.getId(),
                point.getUserId(),
                point.getTotalPoints()
        );
    }
}
