package com.haot.review.application.service;

import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.review.common.exceptions.CustomReviewException;
import com.haot.review.common.response.enums.ErrorCode;
import com.haot.review.domain.model.Review;
import com.haot.review.domain.model.ReviewImage;
import com.haot.review.domain.repository.ReviewImageRepository;
import com.haot.review.domain.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;
  private final MockS3Service mockS3Service;

  @Transactional
  public ReviewGetResponse createReview(String userId, ReviewCreateRequest request) {

    // mock S3 서비스로 이미지를 업로드 및 String List 반환
    List<String> imageUrls = mockS3Service.uploadImages(request.images());

    Review review = Review.createReview(userId, request.contents(), request.lodgeId());
    reviewRepository.save(review);

    List<ReviewImage> reviewImages = imageUrls.stream()
        .map(url -> ReviewImage.create(review, url))
        .toList();

    review.getImages().addAll(reviewImages);
    reviewImageRepository.saveAll(reviewImages);

    return ReviewGetResponse.of(review);
  }

  @Transactional(readOnly = true)
  public ReviewGetResponse readOne(String reviewId) {

    // Todo 삭제 API 구현 후 isDelete 조회 안 되도록 구현할 예정

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomReviewException(ErrorCode.REVIEW_NOT_FOUND));

    return ReviewGetResponse.of(review);
  }
}
