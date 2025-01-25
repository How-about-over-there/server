package com.haot.point.application.dto.request.history;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 만료/사용/만료 내역 검색 REQUEST DTO")
public record PointHistorySearchRequest(
        boolean isEarned,
        boolean isUsed,
        boolean isExpired
) {}
