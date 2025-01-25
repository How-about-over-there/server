package com.haot.point.application.service.impl;

import com.haot.point.application.dto.request.PointTransactionRequest;
import com.haot.point.application.dto.request.point.PointCreateRequest;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointResponse;
import com.haot.point.application.service.PointService;
import com.haot.point.common.exception.CustomPointException;
import com.haot.point.common.exception.enums.ErrorCode;
import com.haot.point.domain.enums.PointStatus;
import com.haot.point.domain.enums.PointType;
import com.haot.point.domain.model.Point;
import com.haot.point.domain.model.PointHistory;
import com.haot.point.domain.utils.CacheEvictUtils;
import com.haot.point.infrastructure.repository.PointHistoryRepository;
import com.haot.point.infrastructure.repository.PointRepository;
import com.haot.submodule.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j(topic = "PointServiceImpl")
@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CacheEvictUtils cacheEvictUtils;

    @Override
    @Transactional
    public PointResponse createPoint(PointCreateRequest request, String userId, Role role) {
        validUser(request.userId(), userId, role);

        if (pointRepository.findByUserIdAndIsDeletedFalse(request.userId()).isPresent()) {
            throw new CustomPointException(ErrorCode.POINT_ALREADY_PRESENT);
        }

        Point point = Point.create(request.userId(), request.totalPoints());
        pointRepository.save(point);
        return PointResponse.of(point);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "point", key = "#userId")
    public PointResponse getPoint(String userId, String headerUserId, Role role) {
        validUser(userId, headerUserId, role);

        Point point = pointRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));
        return PointResponse.of(point);
    }

    @Override
    @Transactional
    public PointAllResponse usePoint(PointTransactionRequest request, String pointId, String userId, Role role) {
        Point point = validPointByUser(pointId, userId, role);

        PointType type = PointType.fromString(request.type());
        if (!(type.equals(PointType.USE))) {
            throw new CustomPointException(ErrorCode.POINT_TYPE_NOT_SUPPORTED);
        }

        if (pointHistoryRepository.findPendingHistory(pointId).isPresent()) {
            throw new CustomPointException(ErrorCode.PENDING_OPERATION_EXISTS);
        }

        if (request.points() > point.getTotalPoints()) {
            throw new CustomPointException(ErrorCode.POINT_INSUFFICIENT);
        }

        point.updateTotalPoint(point.getTotalPoints() - request.points());

        PointHistory pointHistory = PointHistory.create(
                point,
                request.points(),
                type,
                "포인트 사용 대기",
                null,
                PointStatus.PENDING
        );
        pointHistoryRepository.save(pointHistory);
        cacheEvictUtils.evictPoint(userId);
        return PointAllResponse.of(point, pointHistory);
    }

    @Override
    @Transactional
    public PointAllResponse earnPoint(PointTransactionRequest request, String pointId, String userId, Role role) {
        Point point = validPointByUser(pointId, userId, role);

        PointType type = PointType.fromString(request.type());
        if (type != PointType.EARN) {
            throw new CustomPointException(ErrorCode.POINT_TYPE_NOT_SUPPORTED);
        }
        if (request.contextId() == null) {
            throw new CustomPointException(ErrorCode.POINT_CONTEXID_REQUIRED);
        }

        PointHistory pointHistory = PointHistory.create(
                point,
                request.points(),
                type,
                PointType.createDescription(request.contextId(), type),
                LocalDateTime.now().plusMonths(6).with(LocalTime.MAX),
                PointStatus.PROCESSED
        );
        pointHistoryRepository.save(pointHistory);
        point.updateTotalPoint(point.getTotalPoints() + request.points());
        cacheEvictUtils.evictPoint(userId);
        cacheEvictUtils.evictUserPointHistoriesByUserId(userId);
        return PointAllResponse.of(point, pointHistory);
    }

    @Override
    @Transactional
    public boolean expirePoints(LocalDateTime targetDate, int page, int batchSize) {
        LocalDateTime startOfDay = targetDate.toLocalDate().atStartOfDay(); // 자정 시작
        LocalDateTime endOfDay = targetDate.toLocalDate().atTime(LocalTime.MAX); // 자정 끝
        PageRequest pageRequest = PageRequest.of(page, batchSize);

        Page<PointHistory> expiredPoints = pointHistoryRepository.findExpiredPoints(startOfDay, endOfDay, pageRequest);
        expiredPoints.forEach(pointHistory -> processExpiration(pointHistory));

        expiredPoints.forEach(history -> {
            cacheEvictUtils.evictPoint(history.getPoint().getUserId());
            cacheEvictUtils.evictUserPointHistoriesByUserId(history.getPoint().getUserId());
        });

        return !expiredPoints.isEmpty(); // 더 이상 만료할 데이터가 없으면 false 반환
    }

    private void processExpiration(PointHistory pointHistory) {
        double expirePoint = getExpirePoint(pointHistory);

        if (expirePoint > 0) {
            Point point = pointHistory.getPoint();
            point.updateTotalPoint(point.getTotalPoints() - expirePoint);

            PointHistory expireHistory = PointHistory.create(
                    point,
                    expirePoint,
                    PointType.EXPIRE,
                    pointHistory.getDescription() + PointType.EXPIRE.getDescription(),
                    null,
                    PointStatus.PROCESSED
            );
            pointHistoryRepository.save(expireHistory);
        }
    }

    // 만료 포인트 계산
    private double getExpirePoint(PointHistory pointHistory) {
        if (pointHistory.getType() != PointType.EARN || pointHistory.getStatus() != PointStatus.PROCESSED) {
            return 0.0;
        }
        List<PointHistory> usedHistories = pointHistoryRepository.
                findUsedHistories(pointHistory.getPoint().getId(), pointHistory.getCreatedAt());
        double expirePoint = pointHistory.getPoints();

        for (PointHistory usedHistory : usedHistories) {
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

    private void validUser(String requestUserId, String currentUserId, Role role) {
        if (role == Role.USER && !currentUserId.equals(requestUserId)) {
            throw new CustomPointException(ErrorCode.USER_NOT_MATCHED);
        }
    }

    private Point validPointByUser(String pointId, String userId, Role role) {
        Point point = validPoint(pointId);
        validUser(point.getUserId(), userId, role);
        return point;
    }
}
