package com.haot.review.presentation.controller;


import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.review.application.service.ReviewService;
import com.haot.review.common.response.ApiResponse;
import com.haot.review.submodule.role.Role;
import com.haot.review.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

}
