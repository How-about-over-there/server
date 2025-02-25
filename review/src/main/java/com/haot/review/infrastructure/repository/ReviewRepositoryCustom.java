package com.haot.review.infrastructure.repository;


import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.domain.model.Review;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

  Page<Review> searchReview(Role role, ReviewSearchRequest request, Pageable pageable);

}
