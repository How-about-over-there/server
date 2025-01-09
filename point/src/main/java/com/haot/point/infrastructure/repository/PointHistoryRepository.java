package com.haot.point.infrastructure.repository;

import com.haot.point.domain.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, String> {
   @Query("SELECT ph FROM PointHistory ph WHERE ph.point.id = :pointId AND ph.status = 'PENDING'")
    Optional<PointHistory> findPendingHistory(@Param("pointId") String pointId);

}
