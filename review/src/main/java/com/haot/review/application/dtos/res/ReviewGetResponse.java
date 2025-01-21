package com.haot.review.application.dtos.res;

import com.haot.review.domain.model.Review;
import com.haot.review.domain.model.ReviewImage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "리뷰 조회 정보")
@Builder
public record ReviewGetResponse(

    @Schema(description = "리뷰 아이디", example = "review1234")
    String reviewId,
    @Schema(description = "유저 아이디", example = "user1234")
    String userId,
    @Schema(description = "리뷰 내용", example = "정말 좋은 숙소입니다!.")
    String contents,
    @Schema(description = "숙소 아이디", example = "lodge1234")
    String lodgeId,
    @Schema(description = "리뷰 이미지", example = "file1, file2, file3")
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
            .toList())
        .build();
  }
}
