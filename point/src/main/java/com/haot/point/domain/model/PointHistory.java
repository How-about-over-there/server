package com.haot.point.domain.model;

import com.haot.point.domain.enums.PointStatus;
import com.haot.point.domain.enums.PointType;
import com.haot.submodule.auditor.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_point_history", schema = "point")
public class PointHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "point_history_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "points", nullable = false)
    @Min(0)
    private Double points;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PointType type;

    @Column(name = "description")
    private String description;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PointStatus status;

    public static PointHistory create(Point point, Double points, PointType type, String description, LocalDateTime expiredAt, PointStatus status) {
        return PointHistory.builder()
                .point(point)
                .userId(point.getUserId())
                .points(points)
                .type(type)
                .description(description)
                .expiredAt(expiredAt)
                .status(status)
                .build();
    }

    public void updateStatus(String description, PointStatus status) {
        if (description != null) {
            this.description = description;
        }
        this.status = status;
    }
}
