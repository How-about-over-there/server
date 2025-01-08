package com.haot.point.application.service;

import com.haot.point.application.dto.request.PointCreateRequest;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.common.exception.CustomPointException;
import com.haot.point.common.exception.enums.ErrorCode;
import com.haot.point.domain.model.Point;
import com.haot.point.infrastructure.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "PointServiceImpl")
@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;

    @Override
    @Transactional
    public PointResponse createPoint(PointCreateRequest request) {
        // TODO: 1. userId 유효성 검사

        // 2. 포인트 정보 저장
        Point point = Point.create(
                request.userId(),
                request.totalPoints()
        );
        pointRepository.save(point);
        return PointResponse.of(point);
    }

    @Override
    public PointResponse getPoint(String userId) {
        // TODO: 1. 권한 체크 후 userId 설정

        // 2. 기존 point 조회
        Point point = pointRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));

        return PointResponse.of(point);
    }
}
