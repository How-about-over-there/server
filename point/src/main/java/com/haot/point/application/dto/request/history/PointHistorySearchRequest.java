package com.haot.point.application.dto.request.history;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PointHistorySearchRequest {
    private String userId;
    private String type;
    private Boolean isEarn;
    private Boolean isUse;
    private String status;
    private Boolean isUser;
    private Double minPoint;
    private Double maxPoint;
    private LocalDate start;
    private LocalDate end;
}
