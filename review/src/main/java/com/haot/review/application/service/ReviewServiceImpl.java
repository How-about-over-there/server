package com.haot.review.application.service;

import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.application.dtos.req.ReviewUpdateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.review.common.exceptions.CustomReviewException;
import com.haot.review.common.response.enums.ErrorCode;
import com.haot.review.domain.model.Review;
import com.haot.review.domain.model.ReviewImage;
import com.haot.review.domain.repository.ReviewImageRepository;
import com.haot.review.domain.repository.ReviewRepository;
import com.haot.review.presentation.client.LodgeClient;
import com.haot.review.presentation.dto.LodgeReadOneResponse;
import com.haot.submodule.role.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;
  private final MockS3Service mockS3Service;
  private final LodgeClient lodgeClient;

  @Override
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

  @Override
  @Transactional(readOnly = true)
  public ReviewGetResponse readOne(String reviewId) {

    Review review = findActiveReviewById(reviewId);

    return ReviewGetResponse.of(review);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReviewGetResponse> searchReview(Role role, ReviewSearchRequest request, Pageable pageable) {

    return reviewRepository.searchReview(role, request, pageable).map(ReviewGetResponse::of);
  }

  @Override
  @Transactional
  public void updateReview(String reviewId, ReviewUpdateRequest request, String userId, Role role) {

      Review review = findActiveReviewById(reviewId);

      if (!review.getUserId().equals(userId)) {
        throw new CustomReviewException(ErrorCode.FORBIDDEN_OPERATION);
      }
      review.updateReview(request.contents());
  }

  @Override
  @Transactional
  public void deleteReview(String reviewId, String userId, Role role) {

    Review review = findActiveReviewById(reviewId);

    if (!hasPermissionToDeleteReview(review, userId, role)) {
      throw new CustomReviewException(ErrorCode.FORBIDDEN_OPERATION);
    }

    review.deleteReview(userId);
  }

  private Review findActiveReviewById(String reviewId) {
    return reviewRepository.findByReviewIdAndIsDeletedFalse(reviewId)
        .orElseThrow(() -> new CustomReviewException(ErrorCode.REVIEW_NOT_FOUND));
  }

  private boolean hasPermissionToDeleteReview(Review review, String userId, Role role) {
    return switch (role) {
      case USER -> review.getUserId().equals(userId);
      case HOST -> isLodgeHost(userId, review.getLodgeId());
      case ADMIN -> true;
    };
  }

  private boolean isLodgeHost(String hostId, String lodgeId) {
    LodgeReadOneResponse response = lodgeClient.readOne(lodgeId).data();
    return hostId.equals(response.lodge().hostId());
  }
}
