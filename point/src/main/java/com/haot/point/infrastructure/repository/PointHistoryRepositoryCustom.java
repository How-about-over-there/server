package com.haot.point.infrastructure.repository;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.response.PointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryRepositoryCustom {
    Page<PointHistoryResponse> searchPointHistories(PointHistorySearchRequest request, Pageable pageable);
}
