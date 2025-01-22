package com.haot.review.application.service;

import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.application.dtos.req.ReviewUpdateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewService {

  /**
   * 리뷰 생성
   *
   * @param userId  리뷰를 작성한 사용자 ID
   * @param request 리뷰 생성 요청 정보
   * @return 생성된 리뷰 정보
   */
  ReviewGetResponse createReview(String userId, ReviewCreateRequest request);

  /**
   * 리뷰 단건 조회
   *
   * @param reviewId 조회할 리뷰의 ID
   * @return 조회된 리뷰 정보
   */
  ReviewGetResponse readOne(String reviewId);

  /**
   * 리뷰 검색 조회
   *
   * @param role    사용자 역할 (USER, HOST, ADMIN 등)
   * @param request 리뷰 검색 조건
   * @param pageable 페이징 정보
   * @return 검색된 리뷰 정보 목록 (페이징)
   */
  Page<ReviewGetResponse> searchReview(Role role, ReviewSearchRequest request, Pageable pageable);

  /**
   * 리뷰 업데이트
   *
   * @param reviewId 업데이트할 리뷰의 ID
   * @param request  업데이트 요청 정보
   * @param userId   요청을 보낸 사용자 ID
   * @param role     사용자 역할 USER
   */
  void updateReview(String reviewId, ReviewUpdateRequest request, String userId, Role role);

  /**
   * 리뷰 삭제
   *
   * @param reviewId 삭제할 리뷰의 ID
   * @param userId   요청을 보낸 사용자 ID
   * @param role     사용자 역할 (USER, HOST, ADMIN)
   */
  void deleteReview(String reviewId, String userId, Role role);
}
