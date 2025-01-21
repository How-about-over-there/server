package com.haot.point.application.dto.response;

import com.haot.point.domain.model.Point;

import java.io.Serializable;

public record PointResponse(
        String pointId,
        String userId,
        Double totalPoints
) implements Serializable {
    private static final long serialVersionUID = 1L;

    public static PointResponse of(Point point) {
        return new PointResponse(
                point.getId(),
                point.getUserId(),
                point.getTotalPoints()
        );
    }
}
