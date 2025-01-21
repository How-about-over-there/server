package com.haot.point.application.dto.request.history;

public record UserPointHistorySearchRequest(
        boolean isEarned,
        boolean isUsed,
        boolean isExpired
) {}
