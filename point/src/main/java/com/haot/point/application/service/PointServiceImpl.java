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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
        if (!(type.equals(PointType.USE))) {
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

    @Override
    @Transactional
    public PointAllResponse earnPoint(PointTransactionRequest request, String pointId) {

        // 1. 기존 Point 조회
        Point point = validPoint(pointId);

        // 2. 포인트 타입 유효성 검사 및 contextId 확인
        PointType type = PointType.fromString(request.type());
        if (type != PointType.EARN) {
            throw new CustomPointException(ErrorCode.POINT_TYPE_NOT_SUPPORTED);
        }
        if (request.contextId() == null) {
            throw new CustomPointException(ErrorCode.POINT_CONTEXID_REQUIRED);
        }

        // 3. 만료 기간 설정
        // 적립 유형에 따른 포인트 적립 정책이 적용되면 추가 로직 필요
        // 일단 모든 적립 포인트는 6개월 뒤 자정에 만료되도록 설정
        LocalDateTime expiredAt = LocalDateTime.now().plusMonths(6).with(LocalTime.MAX);

        // 3. PointHistory 생성
        PointHistory pointHistory = PointHistory.create(
                point,
                request.points(),
                type,
                PointType.createDescription(request.contextId(), type),
                expiredAt,
                PointStatus.PROCESSED
        );
        pointHistoryRepository.save(pointHistory);

        // 4. 포인트 적립
        point.updateTotalPoint(point.getTotalPoints() + request.points());

        return PointAllResponse.of(point, pointHistory);
    }

    @Override
    @Transactional
    public boolean expirePoints(LocalDateTime targetDate, int page, int batchSize) {
        // 1. 날짜 범위 계산
        LocalDateTime startOfDay = targetDate.toLocalDate().atStartOfDay(); // 자정 시작
        LocalDateTime endOfDay = targetDate.toLocalDate().atTime(LocalTime.MAX); // 자정 끝

        // 2. 페이징 처리
        PageRequest pageRequest = PageRequest.of(page, batchSize);

        // 3. 만료 대상 조회 - isDeleted == false, type == EARN, status == PROCESSED, startOfDay < expiredAt <= endOfDay
        Page<PointHistory> expiredPoints = pointHistoryRepository.findExpiredPoints(
                startOfDay, endOfDay, pageRequest
        );

        // 4. 만료 처리
        for (PointHistory pointHistory : expiredPoints) {
            double expirePoint = getExpirePoint(pointHistory);

            if (expirePoint <= 0) {
                continue;   // 적립 포인트가 모두 사용됨
            }

            // 5. 만료 포인트 차감
            Point point = pointHistory.getPoint();
            point.updateTotalPoint(point.getTotalPoints() - expirePoint);

            // 6. 만료 내역 생성
            PointHistory expireHistory = PointHistory.create(
                    point,
                    expirePoint,
                    PointType.EXPIRE,
                    pointHistory.getDescription() + PointType.EXPIRE.getDescription(),
                    null,
                    PointStatus.PROCESSED
            );

            // 7. 저장
            pointHistoryRepository.save(expireHistory);
        }
        // 8. 페이징 종료 조건
        return !expiredPoints.isEmpty(); // 더 이상 만료할 데이터가 없으면 false 반환
    }

    // 만료 포인트 계산
    public double getExpirePoint(PointHistory pointHistory) {
        if (pointHistory.getType() != PointType.EARN || pointHistory.getStatus() != PointStatus.PROCESSED) {
            return 0.0;
        }

        // 사용 내역 조회 및 남은 포인트 계산
        List<PointHistory> usedHistories = pointHistoryRepository.
                findUsedHistories(pointHistory.getPoint().getId(), pointHistory.getCreatedAt());

        // 포인트 계산
        double expirePoint = pointHistory.getPoints();
        for (PointHistory usedHistory : usedHistories) {
            // 만료 포인트
            double deductPoint = Math.min(expirePoint, usedHistory.getPoints());
            expirePoint -= deductPoint;

            if (expirePoint <= 0) {
                break; // 적립 포인트가 모두 사용됨
            }
        }

        return Math.max(expirePoint, 0.0);
    }

    private Point validPoint(String pointId) {
        return pointRepository.findByIdAndIsDeletedFalse(pointId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));
    }
}
