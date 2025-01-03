package com.haot.lodge.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "p_lodge_image")
public class LodgeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodge_id")
    private Lodge lodge;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "url", nullable = false)
    private String url;

    public static LodgeImage create(
            Lodge lodge,  String url, String title, String description
    ) {
        return LodgeImage.builder()
                .lodge(lodge)
                .title(title)
                .description(description)
                .url(url)
                .build();
    }

}
