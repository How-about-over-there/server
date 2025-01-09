package com.haot.review.domain.model;

import com.haot.review.submodule.auditor.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "p_review")
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "review_id", updatable = false, nullable = false)
  private String reviewId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "lodge_id", nullable = false)
  private String lodgeId;

  @Column(name = "contents", nullable = false)
  private String contents;

  @OneToMany(mappedBy = "review")
  @Builder.Default
  private List<ReviewImage> images = new ArrayList<>();

  public static Review createReview(String userId, String contents, String lodgeId) {
    return Review.builder()
        .userId(userId)
        .lodgeId(lodgeId)
        .contents(contents)
        .build();
  }
  public void deleteReview(String userId) {
    deleteEntity(userId);
  }
}
