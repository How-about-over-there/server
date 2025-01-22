package com.haot.point.application.service;

import com.haot.point.application.dto.request.point.PointCreateRequest;
import com.haot.point.application.dto.request.PointTransactionRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.submodule.role.Role;

import java.time.LocalDateTime;

public interface PointService {

    // 포인트 생성
    PointResponse createPoint(PointCreateRequest request, String userId, Role role);

    // 본인 포인트 조회
    PointResponse getPoint(String userId, String headerUserId, Role role);

    // 포인트 사용
    PointAllResponse usePoint(PointTransactionRequest request, String pointId, String userId, Role role);

    // 포인트 적립
    PointAllResponse earnPoint(PointTransactionRequest request, String pointId, String userId, Role role);

    // 포인트 만료
    boolean expirePoints(LocalDateTime targetDate, int page, int batchSize);
}
