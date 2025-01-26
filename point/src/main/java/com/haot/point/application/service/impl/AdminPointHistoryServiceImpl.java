package com.haot.point.application.service.impl;

import com.haot.point.application.dto.request.history.AdminHistorySearchRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.application.service.AdminPointHistoryService;
import com.haot.point.infrastructure.repository.PointHistoryRepository;
import com.haot.submodule.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "AdminPointHistoryServiceImpl")
@Service
@RequiredArgsConstructor
public class AdminPointHistoryServiceImpl implements AdminPointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PointHistoryResponse> getPointHistories(
            AdminHistorySearchRequest request, Pageable pageable, String userId, Role role) {
        Page<PointHistoryResponse> pointHistories = pointHistoryRepository.searchPointHistories(request, pageable);
        return PageResponse.of(pointHistories);
    }
}
