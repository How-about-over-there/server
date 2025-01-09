package com.haot.review.presentation.controller;


import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.req.ReviewUpdateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.review.application.service.ReviewService;
import com.haot.review.common.response.ApiResponse;
import com.haot.review.common.response.enums.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

  private final ReviewService reviewService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ApiResponse<ReviewGetResponse> create(
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-Role", required = true) String role,
      @Valid ReviewCreateRequest request
  ) {
    return ApiResponse.success(reviewService.createReview(userId, request));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{reviewId}")
  public ApiResponse<ReviewGetResponse> readOne(
      @PathVariable String reviewId
  ) {
    return ApiResponse.success(reviewService.readOne(reviewId));
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{reviewId}")
  public ApiResponse<Void> update(
      @PathVariable String reviewId,
      @Valid @RequestBody ReviewUpdateRequest request,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-Role", required = true) String role
  ) {
    reviewService.updateReview(reviewId, request, userId, role);
    return ApiResponse.success(SuccessCode.UPDATE_REVIEW_SUCCESS);
  }


  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{reviewId}")
  public ApiResponse<Void> delete(
      @PathVariable String reviewId,
      @RequestHeader(value = "X-User-Id", required = true) String userId,
      @RequestHeader(value = "X-Role", required = true) String role
  ) {
    reviewService.deleteReview(reviewId, userId, role);
    return ApiResponse.success(SuccessCode.DELETE_REVIEW_SUCCESS);
  }
}
