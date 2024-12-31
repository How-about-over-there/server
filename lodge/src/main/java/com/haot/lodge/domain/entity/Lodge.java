package com.haot.lodge.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "p_lodge")
public class Lodge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String hostId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer term;

    @Column(nullable = false)
    private Double basicPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodge_rule_id")
    private LodgeRule rule;

    @OneToMany(mappedBy = "lodge")
    @Builder.Default
    private List<LodgeImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "lodge")
    @Builder.Default
    private List<LodgeDate> dates = new ArrayList<>();

}
