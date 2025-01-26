package com.haot.review.presentation.docs;

import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.application.dtos.req.ReviewUpdateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.review.common.response.ApiResponse;
import com.haot.submodule.role.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "Review Management", description = "리뷰 API")
public interface ReviewControllerDocs {

  @Operation(summary = "리뷰 생성", description = "사용자가 리뷰를 작성할 수 있습니다.")
  ApiResponse<ReviewGetResponse> create(
      String userId,
      Role role,
      ReviewCreateRequest request
  );

  @Operation(summary = "리뷰 단건 조회", description = "사용자가 리뷰를 조회할 수 있습니다.")
  ApiResponse<ReviewGetResponse> readOne(
      String reviewId
  );

  @Operation(summary = "리뷰 검색 조회", description = "사용자가 검색 조건에 따라 리뷰를 조회할 수 있습니다.")
  ApiResponse<Page<ReviewGetResponse>> search(
      Role role,
      ReviewSearchRequest request,
      Pageable pageable
  );

  @Operation(summary = "리뷰 수정", description = "사용자가 리뷰를 수정할 수 있습니다.")
  ApiResponse<Void> update(
      String reviewId,
      ReviewUpdateRequest request,
      String userId,
      Role role
  );

  @Operation(summary = "리뷰 삭제", description = "사용자가 리뷰를 삭제할 수 있습니다.")
  ApiResponse<Void> delete(
      String reviewId,
      String userId,
      Role role
  );
}
