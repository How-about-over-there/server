package com.haot.point.application.service.impl;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.application.service.PointHistoryService;
import com.haot.point.common.exception.CustomPointException;
import com.haot.point.common.exception.enums.ErrorCode;
import com.haot.point.domain.enums.PointStatus;
import com.haot.point.domain.enums.PointType;
import com.haot.point.domain.model.Point;
import com.haot.point.domain.model.PointHistory;
import com.haot.point.domain.utils.CacheEvictUtils;
import com.haot.point.infrastructure.repository.PointHistoryRepository;
import com.haot.submodule.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j(topic = "PointHistoryServiceImpl")
@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final CacheManager cacheManager;
    private final CacheEvictUtils cacheEvictUtils;

    @Override
    @Transactional
    public PointAllResponse updateStatusPoint(PointStatusRequest request, String historyId, String userId, Role role) {
        PointHistory pointHistory = validPointHistoryByUser(historyId, userId, role);

        PointStatus status = PointStatus.fromString(request.status());
        validateStatusTransition(pointHistory, status, request.cancelType());

        if (request.cancelType() != null) {
            PointType cancelType = PointType.fromString(request.cancelType());
            return createCancelData(pointHistory, cancelType, request.contextId());
        }

        if (status == PointStatus.ROLLBACK) {
            handleRollback(pointHistory);
        } else {
            String description = PointType.createDescription(request.contextId(), pointHistory.getType());
            pointHistory.updateStatus(description, status);
        }

        cacheEvictUtils.evictPoint(userId);
        cacheEvictUtils.evictPointHistory(historyId);
        cacheEvictUtils.evictUserPointHistoriesByUserId(userId);

        return PointAllResponse.of(pointHistory.getPoint(), pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "pointHistory", key = "#historyId")
    public PointHistoryResponse getPointHistoryById(String historyId, String userId, Role role) {
        PointHistory pointHistory = validPointHistoryByUser(historyId, userId, role);
        if (role == Role.USER) {
            if (pointHistory.getStatus() == PointStatus.PENDING || pointHistory.getStatus() == PointStatus.ROLLBACK) {
                throw new CustomPointException(ErrorCode.UNAUTHORIZED_EXCEPTION);
            }
        }
        return PointHistoryResponse.of(pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "userPointHistories",
            key = "T(String).format('%s:%s:%s:%s:%s:%s', #userId, #pageable.pageNumber, #pageable.pageSize, #request.isEarned, #request.isUsed, #request.isExpired)"
    )
    public PageResponse<PointHistoryResponse> getUserPointHistories(
            PointHistorySearchRequest request, Pageable pageable, String userId) {

        String fullListCacheKey = String.format("%s:%d:%d:false:false:false", userId, pageable.getPageNumber(), pageable.getPageSize());

        PageResponse<PointHistoryResponse> fullListCachedData = getCachedDataFromCacheManager("userPointHistories", fullListCacheKey);
        if (fullListCachedData == null || fullListCachedData.content().isEmpty()) {
            log.info("전체 목록 DB 조회 후 캐싱 처리");
            fullListCachedData = cacheUserPointHistories(userId, pageable); // DB 조회 후 전체 목록 캐싱
            saveToCache("userPointHistories", fullListCacheKey, fullListCachedData); // 전체 목록 저장
        }

        return PageResponse.of(filterAndPaginate(fullListCachedData, request, pageable));
    }

    /* DB 에서 데이터를 조회하고 캐싱 */
    @Transactional(readOnly = true)
    public PageResponse<PointHistoryResponse> cacheUserPointHistories(String userId, Pageable pageable) {
        List<PointStatus> statuses = List.of(PointStatus.PROCESSED, PointStatus.CANCELLED);
        Page<PointHistory> pointHistories = pointHistoryRepository.findByUserIdAndStatusInAndIsDeletedFalse(userId, statuses, pageable);
        return PageResponse.of(pointHistories.map(PointHistoryResponse::of));
    }

    /* 캐싱 데이터를 명시적으로 저장 */
    private void saveToCache(String cacheName, String key, PageResponse<PointHistoryResponse> data) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, data); // 명시적으로 캐시 저장
        }
    }

    /* 캐싱된 데이터를 CacheManager 를 통해 확인 */
    private PageResponse<PointHistoryResponse> getCachedDataFromCacheManager(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            return cache.get(key, PageResponse.class); // 캐싱된 데이터 반환
        }
        return null; // 캐시에 데이터가 없으면 null 반환
    }

    /* 조건에 따라 데이터를 필터링하고 페이징 처리 */
    private Page<PointHistoryResponse> filterAndPaginate(PageResponse<PointHistoryResponse> cachedData,
                                                         PointHistorySearchRequest request,
                                                         Pageable pageable) {
        Stream<PointHistoryResponse> filteredData = cachedData.content().stream();

        if (request.isEarned()) {
            filteredData = filteredData.filter(item -> item.type().equals("EARN") || item.type().equals("CANCEL_USE"));
        }
        if (request.isUsed()) {
            filteredData = filteredData.filter(item -> item.type().equals("USE") || item.type().equals("CANCEL_EARN"));
        }
        if (request.isExpired()) {
            filteredData = filteredData.filter(item -> item.type().equals("EXPIRED"));
        }

        List<PointHistoryResponse> filteredList = filteredData.collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredList.size());

        return new PageImpl<>(filteredList.subList(start, end), pageable, filteredList.size());
    }

    /* 타입에 따른 롤백 처리 로직 */
    private void handleRollback(PointHistory pointHistory) {
        Point point = pointHistory.getPoint();
        switch (pointHistory.getType()) {
            case USE, CANCEL_EARN ->
                    point.updateTotalPoint(point.getTotalPoints() + pointHistory.getPoints()); // 포인트 복원, 적립 차감 복원
            case EARN, CANCEL_USE ->
                    point.updateTotalPoint(point.getTotalPoints() - pointHistory.getPoints()); // 적립된 포인트 차감, 사용 복원 취소
            default -> throw new CustomPointException(ErrorCode.POINT_TYPE_NOT_SUPPORTED);
        }
    }

    /* 취소 데이터 생성 및 캐시 삭제 */
    private PointAllResponse createCancelData(PointHistory pointHistory, PointType pointType, String contextId) {
        Point point = pointHistory.getPoint();
        pointHistory.updateStatus(null, PointStatus.CANCELLED);
        PointHistory cancelHistory = PointHistory.create(
                point,
                pointHistory.getPoints(), // 기존 포인트 값 사용
                pointType,
                PointType.createDescription(contextId, pointType),
                null,
                PointStatus.PROCESSED
        );
        pointHistoryRepository.save(cancelHistory);

        if (pointType.equals(PointType.CANCEL_USE)) {
            point.updateTotalPoint(point.getTotalPoints() + cancelHistory.getPoints()); // 복원
        } else if (pointType.equals(PointType.CANCEL_EARN)) {
            point.updateTotalPoint(point.getTotalPoints() - cancelHistory.getPoints()); // 차감
        }

        cacheEvictUtils.evictPoint(point.getUserId());
        cacheEvictUtils.evictPointHistory(pointHistory.getId());
        cacheEvictUtils.evictUserPointHistoriesByUserId(point.getUserId());
        return PointAllResponse.of(point, cancelHistory);
    }

    private PointHistory validPointHistoryByUser(String historyId, String userId, Role role) {
        PointHistory pointHistory = pointHistoryRepository.findByIdAndIsDeletedFalse(historyId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));

        if (role == Role.USER && !pointHistory.getUserId().equals(userId)) {
            throw new CustomPointException(ErrorCode.USER_NOT_MATCHED);
        }
        return pointHistory;
    }

    private void validateStatusTransition(PointHistory pointHistory, PointStatus status, String cancelType) {
        if (pointHistory.getStatus() == PointStatus.ROLLBACK || pointHistory.getStatus() == PointStatus.CANCELLED) {
            throw new CustomPointException(ErrorCode.POINT_CHANGE_NOT_SUPPORTED);
        }

        if (status == PointStatus.CANCELLED || status == PointStatus.PENDING) {
            throw new CustomPointException(ErrorCode.POINT_CHANGE_NOT_SUPPORTED);
        }

        if (cancelType != null) {
            PointType cancelPointType = PointType.fromString(cancelType);
            if (pointHistory.getStatus() == PointStatus.PENDING) {
                throw new CustomPointException(ErrorCode.PENDING_OPERATION_EXISTS);
            }

            if (status == PointStatus.ROLLBACK) {
                throw new CustomPointException(ErrorCode.INVALID_REQUEST_COMBINATION);
            }

            if (pointHistory.getType() == PointType.USE && cancelPointType == PointType.CANCEL_USE) {
                throw new CustomPointException(ErrorCode.INVALID_CANCEL_TYPE_FOR_USE);
            }
            if (pointHistory.getType() == PointType.EARN && cancelPointType == PointType.CANCEL_EARN) {
                throw new CustomPointException(ErrorCode.INVALID_CANCEL_TYPE_FOR_EARN);
            }
        }
    }
}
