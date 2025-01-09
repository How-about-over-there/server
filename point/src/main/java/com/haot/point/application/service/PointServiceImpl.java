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

import java.util.Optional;

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

        // 1. 포인트 타입 유효성 검사
        PointType type = PointType.fromString(request.type());
        if (!(type.equals(PointType.USE) || type.equals(PointType.REFUND))) {
            throw new CustomPointException(ErrorCode.POINT_TYPE_NOT_SUPPORTED);
        }

        // 2. Pending 상태 확인
        Optional<PointHistory> pendingHistory = pointHistoryRepository.findPendingHistory(pointId);
        if (pendingHistory.isPresent()) {
            throw new CustomPointException(ErrorCode.PENDING_OPERATION_EXISTS);
        }

        // 3. 기존 Point 조회 및 잔액 확인
        Point point = validPoint(pointId);
        if (request.points() > point.getTotalPoints()) {
            throw new CustomPointException(ErrorCode.POINT_INSUFFICIENT);
        }

        // 4. 포인트 차감
        point.updateTotalPoint(point.getTotalPoints() - request.points());

        // 5. PointHistory 생성
        PointHistory pointHistory = PointHistory.create(
                point,
                request.points(),
                type,
                "포인트 사용 대기",
                null,
                PointStatus.PENDING
        );
        pointHistoryRepository.save(pointHistory);

        return PointAllResponse.of(point, pointHistory);
    }


    // 포인트 설명 생성
    public String createDescription(String contextId, PointType type) {
        return contextId + " " + type.getDescription() + " 포인트";
    }

    private Point validPoint(String pointId) {
        return pointRepository.findByIdAndIsDeletedFalse(pointId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));
    }
}
