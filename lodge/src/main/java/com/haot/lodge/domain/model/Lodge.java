package com.haot.lodge.domain.model;


import jakarta.persistence.CascadeType;
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

    @Column(name = "host_id", nullable = false)
    private String hostId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "term", nullable = false)
    private Integer term;

    @Column(name = "basic_price", nullable = false)
    private Double basicPrice;

    @OneToMany(mappedBy = "lodge")
    @Builder.Default
    private List<LodgeImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "lodge")
    @Builder.Default
    private List<LodgeDate> dates = new ArrayList<>();

    public static Lodge createLodge(
            String hostId, String name, String description, String address, Integer term, Double basicPrice
    ) {
        return Lodge.builder()
                .hostId(hostId)
                .name(name)
                .description(description)
                .address(address)
                .term(term)
                .basicPrice(basicPrice)
                .build();
    }

}
