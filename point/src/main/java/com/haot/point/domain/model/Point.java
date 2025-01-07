package com.haot.point.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_point", schema = "point")
public class Point extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "point_id")
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "total_points", nullable = false)
    @Min(0)
    private Double totalPoints;
}
