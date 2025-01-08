package com.haot.point.infrastructure.repository;

import com.haot.point.domain.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point,String> {
    Optional<Point> findByUserIdAndIsDeletedFalse(String userId);

    Optional<Point> findByIdAndIsDeletedFalse(String pointId);
}
