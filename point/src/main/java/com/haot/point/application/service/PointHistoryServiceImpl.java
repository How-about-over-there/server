package com.haot.point.application.service;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.request.history.UserPointHistorySearchRequest;
import com.haot.point.application.dto.request.point.PointStatusRequest;
import com.haot.point.application.dto.response.PageResponse;
import com.haot.point.application.dto.response.PointAllResponse;
import com.haot.point.application.dto.response.PointHistoryResponse;
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

        // 1. 기존 point 내역 조회
        PointHistory pointHistory = validPointHistory(historyId);
        Point point = pointHistory.getPoint();

        // userId 검증
        if (role == Role.USER && !pointHistory.getUserId().equals(userId)) {
                throw new CustomPointException(ErrorCode.USER_NOT_MATCHED);
        }

        // 2. 현재 상태가 ROLLBACK 또는 CANCELLED 인 경우 상태 전이 불가
        if (pointHistory.getStatus().equals(PointStatus.ROLLBACK) || pointHistory.getStatus().equals(PointStatus.CANCELLED)) {
            throw new CustomPointException(ErrorCode.POINT_CHANGE_NOT_SUPPORTED);
        }

        // 3. status 유효성 검사 및 상태 변경 가능 여부 검증
        PointStatus status = PointStatus.fromString(request.status());
        // PENDING 상태에서 cancelType 은 null 이어야 함 -> 취소 요청 불가
        if (pointHistory.getStatus().equals(PointStatus.PENDING) && request.cancelType() != null) {
            throw new CustomPointException(ErrorCode.PENDING_OPERATION_EXISTS);
        }
        // CANCELLED 와 PENDING 상태는 외부에서 요청 불가
        if (status.equals(PointStatus.CANCELLED) || status.equals(PointStatus.PENDING)) {
            throw new CustomPointException(ErrorCode.POINT_CHANGE_NOT_SUPPORTED);
        }

        // 4. 요청 type, status 에 따른 처리 - 1) 취소 요청에 대한 처리
        // cancelType != null && status == PROCESSED
        if (request.cancelType() != null) {
            PointType cancelType = PointType.fromString(request.cancelType());

            // (1) USE 타입에선 CANCEL_USE 만 허용
            if (pointHistory.getType().equals(PointType.USE) && !cancelType.equals(PointType.CANCEL_USE)) {
                throw new CustomPointException(ErrorCode.INVALID_CANCEL_TYPE_FOR_USE);
            }
            // (2) EARN 타입에선 CANCEL_EARN 만 허용
            if (pointHistory.getType().equals(PointType.EARN) && !cancelType.equals(PointType.CANCEL_EARN)) {
                throw new CustomPointException(ErrorCode.INVALID_CANCEL_TYPE_FOR_EARN);
            }
            // (3) cancelType 과 ROLLBACK 조합은 허용하지 않음
            if (status.equals(PointStatus.ROLLBACK)) {
                throw new CustomPointException(ErrorCode.INVALID_REQUEST_COMBINATION);
            }
            // 새로운 취소 데이터 생성
            return createCancelData(point, pointHistory, cancelType, request.contextId());
        }

        // 4. 요청 type, status 에 따른 처리 - 2) 실패로 인한 롤백
        // cancelType == null && status == ROLLBACK
        if (status.equals(PointStatus.ROLLBACK)) {
            handleRollback(point, pointHistory);
        }

        // 4. 요청 type, status 에 따른 처리 - 3) 단순 상태 변경
        // cancelType == null && status == PROCESSED
        String description = PointType.createDescription(request.contextId(), pointHistory.getType());
        pointHistory.updateStatus(description, status);

        // 캐시 삭제
        cacheEvictUtils.evictPoint(userId);
        cacheEvictUtils.evictPointHistory(historyId);
        cacheEvictUtils.evictUserPointHistoriesByUserId(userId);

        return PointAllResponse.of(point, pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "pointHistory", key = "#historyId")
    public PointHistoryResponse getPointHistoryById(String historyId, String userId, Role role) {
        // 1. 기존 point 내역 조회
        PointHistory pointHistory = validPointHistory(historyId);
        // userId 검증
        if (role == Role.USER) {
            if (!pointHistory.getUserId().equals(userId)) {
                throw new CustomPointException(ErrorCode.USER_NOT_MATCHED);
            }
            if (pointHistory.getStatus() == PointStatus.PENDING
            || pointHistory.getStatus() == PointStatus.ROLLBACK) {
                throw new CustomPointException(ErrorCode.UNAUTHORIZED_EXCEPTION);
            }
        }
        return PointHistoryResponse.of(pointHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PointHistoryResponse> getPointHistories(
            PointHistorySearchRequest request, Pageable pageable, String userId, Role role) {
        // USER 요청의 경우
        if (role == Role.USER) {
            request.setUserId(userId);
            request.setStatus("PROCESSED");
        }
        Page<PointHistoryResponse> pointHistories = pointHistoryRepository.searchPointHistories(request, pageable);
        return PageResponse.of(pointHistories);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "userPointHistories",
            key = "T(String).format('%s:%s:%s:%s:%s:%s', #userId, #pageable.pageNumber, #pageable.pageSize, #request.isEarned, #request.isUsed, #request.isExpired)"
    )
    public PageResponse<PointHistoryResponse> getUserPointHistories(
            UserPointHistorySearchRequest request, Pageable pageable, String userId) {

        // 전체 목록 캐싱 키 정의
        String fullListCacheKey = String.format("%s:%d:%d:false:false:false", userId, pageable.getPageNumber(), pageable.getPageSize());

        // 1. 전체 목록 캐싱 확인
        PageResponse<PointHistoryResponse> fullListCachedData = getCachedDataFromCacheManager("userPointHistories", fullListCacheKey);
        if (fullListCachedData == null || fullListCachedData.content().isEmpty()) {
            log.info("전체 목록 DB 조회 후 캐싱 처리");
            fullListCachedData = cacheUserPointHistories(userId, pageable); // DB 조회 후 전체 목록 캐싱
            saveToCache("userPointHistories", fullListCacheKey, fullListCachedData); // 전체 목록 저장
        }

        // 2. 조건에 따라 필터링 처리
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
                                                         UserPointHistorySearchRequest request,
                                                         Pageable pageable) {
        Stream<PointHistoryResponse> filteredData = cachedData.content().stream();

        // 조건별 필터링
        if (request.isEarned()) {
            filteredData = filteredData.filter(item -> item.type().equals("EARN") || item.type().equals("CANCEL_USE"));
        }
        if (request.isUsed()) {
            filteredData = filteredData.filter(item -> item.type().equals("USE") || item.type().equals("CANCEL_EARN"));
        }
        if (request.isExpired()) {
            filteredData = filteredData.filter(item -> item.type().equals("EXPIRED"));
        }

        // 필터링된 데이터를 리스트로 변환
        List<PointHistoryResponse> filteredList = filteredData.collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredList.size());

        return new PageImpl<>(filteredList.subList(start, end), pageable, filteredList.size());
    }

    private PointHistory validPointHistory(String historyId) {
        return pointHistoryRepository.findByIdAndIsDeletedFalse(historyId)
                .orElseThrow(() -> new CustomPointException(ErrorCode.POINT_NOT_FOUND));
    }

    // 타입에 따른 롤백 처리 로직
    private void handleRollback(Point point, PointHistory pointHistory) {
        switch (pointHistory.getType()) {
            // 포인트 복원, 적립 차감 복원
            case USE, CANCEL_EARN ->
                    point.updateTotalPoint(point.getTotalPoints() + pointHistory.getPoints());
            // 적립된 포인트 차감, 사용 복원 취소
            case EARN, CANCEL_USE ->
                    point.updateTotalPoint(point.getTotalPoints() - pointHistory.getPoints());
            default -> throw new CustomPointException(ErrorCode.POINT_TYPE_NOT_SUPPORTED);
        }
    }

    // 취소 데이터 생성
    private PointAllResponse createCancelData(Point point, PointHistory pointHistory, PointType pointType, String contextId) {
        // 기존 포인트 내역 상태 CANCELLED 으로 변경
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

        // 포인트 업데이트
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
}
