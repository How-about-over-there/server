package com.haot.point.application.service;

import com.haot.point.application.dto.request.PointCreateRequest;
import com.haot.point.application.dto.response.PointResponse;

public interface PointService {

    // 포인트 생성
    PointResponse createPoint(PointCreateRequest request);

    // 본인 포인트 조회
    PointResponse getPoint(String userId);
}
