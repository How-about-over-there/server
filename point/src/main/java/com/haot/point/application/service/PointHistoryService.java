package com.haot.point.application.service;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {

    PointAllResponse updateStatusPoint(PointStatusRequest request, String historyId, String userId, Role role);

    PointHistoryResponse getPointHistoryById(String historyId, String userId, Role role);

    PageResponse<PointHistoryResponse> getUserPointHistories(
            PointHistorySearchRequest request,
            Pageable pageable,
            String userId);
}
