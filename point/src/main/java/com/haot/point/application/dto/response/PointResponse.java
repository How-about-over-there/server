package com.haot.point.application.dto.response;

public record PointResponse(
        String pointId,
        String userId,
        Double totalPoints
) {}
