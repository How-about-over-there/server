package com.haot.point.application.dto.response;

import com.haot.point.domain.model.Point;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "포인트 RESPONSE DTO")
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
