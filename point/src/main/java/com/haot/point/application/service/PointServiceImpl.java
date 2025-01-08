package com.haot.point.application.service;

import com.haot.point.application.dto.request.PointCreateRequest;
import com.haot.point.application.dto.request.PointTransactionRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.common.exception.CustomPointException;
import com.haot.point.common.exception.enums.ErrorCode;
import com.haot.point.domain.enums.PointStatus;
import com.haot.point.domain.enums.PointType;
import com.haot.point.domain.model.Point;
import com.haot.point.domain.model.PointHistory;
import com.haot.point.infrastructure.repository.PointHistoryRepository;
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
    private final PointHistoryRepository pointHistoryRepository;

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
    @Transactional(readOnly = true)
    public PointResponse getPoint(String userId) {
        // TODO: 1. 권한 체크 후 userId 설정

        // 2. 기존 point 조회
        Point point = pointRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));

        return PointResponse.of(point);
    }

    @Override
    @Transactional
    public PointAllResponse usePoint(PointTransactionRequest request, String pointId) {

        // 1. 기존 Point 조회
        Point point = validPoint(pointId);

        // 2. 포인트 잔액 확인
        if (request.points() > point.getTotalPoints()) {
            throw new CustomPointException(ErrorCode.POINT_INSUFFICIENT);
        }

        // 3. 포인트 타입 유효성 검사
        PointType type = PointType.fromString(request.type());
        String description = "";
        PointStatus status = PointStatus.PENDING;

        // 4. 포인트 타입에 따라 처리
        if (type.equals(PointType.USE)) {
            // TODO : 동시성 제어(DB 락 or Redis or Kafka 필요)
        } else {
            if (request.contextId() == null) {
                throw new CustomPointException(ErrorCode.POINT_CONTEXID_REQUIRED);
            }
            description = createDescription(request.contextId(), type);
            status = PointStatus.PROCESSED;
            // 보유 포인트 차감
            point.updateTotalPoint(point.getTotalPoints() - request.points());
        }

        // 5. 포인트 내역 저장
        PointHistory pointHistory = PointHistory.create(
                point,
                request.points(),
                type,
                description,
                null,
                status
        );
        pointHistoryRepository.save(pointHistory);
        return PointAllResponse.of(point, pointHistory);
    }

    // 포인트 설명 생성
    public String createDescription(String contextId, PointType type) {
        return contextId + " " + type + " 포인트";
    }

    private Point validPoint(String pointId) {
        return pointRepository.findByIdAndIsDeletedFalse(pointId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));
    }
}
