package com.haot.point.infrastructure.repository;

import com.haot.point.domain.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, String> {
}
