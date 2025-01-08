package com.haot.review.application.dtos.res;

import com.haot.review.domain.model.Review;
import com.haot.review.domain.model.ReviewImage;
import java.util.List;
import lombok.Builder;

@Builder
public record ReviewGetResponse(

    String reviewId,
    String userId,
    String contents,
    String lodgeId,
    List<String> imageUrls

) {

  public static ReviewGetResponse of(Review review) {
    return ReviewGetResponse.builder()
        .reviewId(review.getReviewId())
        .userId(review.getUserId())
        .contents(review.getContents())
        .lodgeId(review.getLodgeId())
        .imageUrls(review.getImages().stream()
            .map(ReviewImage::getUrl)
            .toList()
        )
        .build();
  }

}
