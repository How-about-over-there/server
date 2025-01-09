package com.haot.review.domain.repository;

import com.haot.review.domain.model.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {

  Optional<Review> findByReviewIdAndIsDeletedFalse(String reviewId);

}
