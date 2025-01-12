package com.haot.point.domain.model;

import com.haot.submodule.auditor.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_point", schema = "point")
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "point_id")
    private String id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "total_points", nullable = false)
    @Min(0)
    private Double totalPoints;

    public static Point create(String userId, Double totalPoints) {
        return Point.builder()
                .userId(userId)
                .totalPoints(totalPoints)
                .build();
    }

    public void updateTotalPoint(Double totalPoints) {
        this.totalPoints = totalPoints;
    }
}
