package com.haot.point.application.service;

import com.haot.point.application.dto.request.history.AdminHistorySearchRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Pageable;

public interface AdminPointHistoryService {

    PageResponse<PointHistoryResponse> getPointHistories(
            AdminHistorySearchRequest request,
            Pageable pageable,
            String userId,
            Role role);
}
