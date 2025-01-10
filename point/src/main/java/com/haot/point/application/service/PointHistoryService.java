package com.haot.point.application.service;

import com.haot.point.application.dto.request.PointStatusRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.submodule.role.Role;

public interface PointHistoryService {

    // 포인트 상태 변경
    PointAllResponse updateStatusPoint(PointStatusRequest request, String historyId, String userId, Role role);
}
