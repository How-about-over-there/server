package com.haot.lodge.domain.model;


import com.haot.submodule.auditor.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_lodge_rule", schema = "lodge")
public class LodgeRule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodge_id")
    private Lodge lodge;

    @Column(name = "max_reservation_day", nullable = false)
    private Integer maxReservationDay;

    @Column(name = "max_personnel", nullable = false)
    private Integer maxPersonnel;

    @Column(name = "customization")
    private String customization;


    public static LodgeRule create(
            Lodge lodge, Integer maxReservationDay, Integer maxPersonnel, String customization
    ) {
        return LodgeRule.builder()
                .lodge(lodge)
                .maxReservationDay(maxReservationDay)
                .maxPersonnel(maxPersonnel)
                .customization(customization)
                .build();
    }
}
