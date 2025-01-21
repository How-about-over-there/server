package com.haot.review.application;

import com.haot.review.domain.model.Review;
import org.springframework.test.util.ReflectionTestUtils;

public class TestMockData {

  public static Review testReview() {

    String testLodgeId = "testLodgeId";
    String testUserId = "testUserId";
    String testContent = "testContent";

    Review testReview = Review.createReview(testLodgeId, testUserId, testContent);
    ReflectionTestUtils.setField(testReview, "reviewId", "testReviewId");

    return testReview;
  }

}
