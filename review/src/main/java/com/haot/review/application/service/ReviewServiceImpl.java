package com.haot.review.application.service;

import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.application.dtos.req.ReviewUpdateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.review.common.exceptions.CustomReviewException;
import com.haot.review.common.response.enums.ErrorCode;
import com.haot.review.domain.model.Review;
import com.haot.review.domain.model.ReviewImage;
import com.haot.review.domain.repository.ReviewRepository;
import com.haot.review.infrastructure.client.LodgeClient;
import com.haot.review.infrastructure.dto.LodgeReadOneResponse;
import com.haot.submodule.role.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final S3Service s3Service;
  private final LodgeClient lodgeClient;
  private final ReviewRepository reviewRepository;

  @Override
  @Transactional
  public ReviewGetResponse createReview(String userId, ReviewCreateRequest request) {

    Review review = Review.createReview(userId, request.contents(), request.lodgeId());
    reviewRepository.save(review);

    if (request.images() != null && !request.images().isEmpty()) {
      List<ReviewImage> reviewImages = request.images().stream()
          .map(image -> {
            String imageUrl = s3Service.convertToUrl(image);
            return ReviewImage.create(review, imageUrl);
          })
          .toList();

      review.getImages().addAll(reviewImages);
    }
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
  public Page<ReviewGetResponse> searchReview(Role role, ReviewSearchRequest request,
      Pageable pageable) {

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
    review.getImages().forEach(reviewImage -> reviewImage.deleteReviewImage(userId));
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
