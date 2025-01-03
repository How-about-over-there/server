package com.haot.point.infrastructure.repository;

import com.haot.point.domain.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point,String> {
}
