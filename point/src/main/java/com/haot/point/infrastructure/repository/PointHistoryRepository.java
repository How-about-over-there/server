package com.haot.point.infrastructure.repository;

import com.haot.point.domain.enums.PointStatus;
import com.haot.point.domain.model.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, String> , PointHistoryRepositoryCustom{
    @Query("SELECT ph FROM PointHistory ph WHERE ph.point.id = :pointId AND ph.status = 'PENDING'")
    Optional<PointHistory> findPendingHistory(@Param("pointId") String pointId);

    Optional<PointHistory> findByIdAndIsDeletedFalse(String historyId);

    Page<PointHistory> findByUserIdAndStatusAndIsDeletedFalse(String userId, PointStatus status, Pageable pageable);

    // 만료 대상 적립 내역 조회
    @Query("SELECT h FROM PointHistory h WHERE h.type = 'EARN' AND h.status = 'PROCESSED'" +
            "AND h.expiredAt BETWEEN :startOfDay AND :endOfDay AND h.isDeleted = false")
    Page<PointHistory> findExpiredPoints(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable
    );

    // 만료 대상 이후의 사용 포인트 조회
    @Query("SELECT h FROM PointHistory h WHERE h.point.id = :pointId AND h.type = 'USE' AND h.status = 'PROCESSED'" +
            "AND h.createdAt > :createdAt AND h.isDeleted = false ORDER BY h.createdAt ASC")
    List<PointHistory> findUsedHistories(
            @Param("pointId") String pointId,
            @Param("createdAt") LocalDateTime createdAt
    );

}
