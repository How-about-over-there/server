package com.haot.point.application.service;

import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.submodule.role.Role;

public interface PointHistoryService {

    // 포인트 상태 변경
    PointAllResponse updateStatusPoint(PointStatusRequest request, String historyId, String userId, Role role);

    // 본인 포인트 내역 단건 조회
    PointHistoryResponse getPointHistoryById(String historyId, String userId, Role role);
}
