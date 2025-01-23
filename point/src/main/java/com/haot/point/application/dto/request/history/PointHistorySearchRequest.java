package com.haot.point.application.dto.request.history;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "포인트 내역 검색 REQUEST DTO")
@Data
public class PointHistorySearchRequest {
    private String userId;
    private Boolean isUser;
    private Boolean isEarn;
    private Boolean isUse;
    @Schema(description = "USE, EARN, CANCEL_USE, CANCEL_EARN, EXPIRE 만 요청 가능합니다.")
    private String type;
    @Schema(description = "PENDING, PROCESSED, ROLLBACK, CANCELLED 만 요청 가능합니다.")
    private String status;
    private Double minPoint;
    private Double maxPoint;
    private LocalDate start;
    private LocalDate end;
}
