package com.haot.point.application.service;

import com.haot.point.application.dto.request.point.PointCreateRequest;
import com.haot.point.application.dto.request.PointTransactionRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.submodule.role.Role;

import java.time.LocalDateTime;

public interface PointService {

    PointResponse createPoint(PointCreateRequest request, String userId, Role role);

    PointResponse getPoint(String userId, String headerUserId, Role role);

    PointAllResponse usePoint(PointTransactionRequest request, String pointId, String userId, Role role);

    PointAllResponse earnPoint(PointTransactionRequest request, String pointId, String userId, Role role);

    boolean expirePoints(LocalDateTime targetDate, int page, int batchSize);
}
