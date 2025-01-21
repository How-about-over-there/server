package com.haot.review.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.haot.review.application.TestMockData;
import com.haot.review.application.dtos.req.ReviewCreateRequest;
import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.application.dtos.req.ReviewUpdateRequest;
import com.haot.review.application.dtos.res.ReviewGetResponse;
import com.haot.review.common.exceptions.CustomReviewException;
import com.haot.review.domain.model.Review;
import com.haot.review.domain.repository.ReviewRepository;
import com.haot.review.presentation.client.LodgeClient;
import com.haot.submodule.role.Role;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @InjectMocks
  private ReviewServiceImpl reviewService;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private LodgeClient lodgeClient;
  @Mock
  private S3Service s3Service;

  @Nested
  public class createReview {

    @Test
    @DisplayName("리뷰 생성 성공 테스트")
    void createReview_Success() {

      // given
      String testUserId = "testUserId";
      String testContent = "testContent";
      String testLodgeId = "testLodgeId";

      MultipartFile file1 = mock(MultipartFile.class);
      MultipartFile file2 = mock(MultipartFile.class);
      given(file1.getOriginalFilename()).willReturn("image1.jpg");
      given(file2.getOriginalFilename()).willReturn("image2.jpg");

      List<MultipartFile> files = List.of(file1, file2);

      ReviewCreateRequest request = new ReviewCreateRequest(testContent, testLodgeId, files);
      Review testReview = Review.createReview(testUserId, testContent, testLodgeId);

      given(reviewRepository.save(any(Review.class))).willReturn(testReview);
      given(s3Service.convertToUrl(any(MultipartFile.class))).willAnswer(invocation ->
          "http://mocked.url/" + ((MultipartFile) invocation.getArgument(0)).getOriginalFilename()
      );

      // when
      ReviewGetResponse response = reviewService.createReview(testUserId, request);

      // then
      assertEquals(testContent, response.contents());
      assertEquals(2, response.imageUrls().size());
      verify(reviewRepository, times(1)).save(any(Review.class));

      assertTrue(response.imageUrls().contains("http://mocked.url/image1.jpg"));
      assertTrue(response.imageUrls().contains("http://mocked.url/image2.jpg"));
      verify(s3Service, times(2)).convertToUrl(any(MultipartFile.class));
    }
  }

  @Nested
  public class readOne {

    @Test
    @DisplayName("리뷰 단건 조회 성공 테스트")
    void readOne_Success() {

      // given
      Review testReview = TestMockData.testReview();
      given(reviewRepository.findByReviewIdAndIsDeletedFalse(anyString())).willReturn(
          Optional.of(testReview));

      // when
      ReviewGetResponse response = reviewService.readOne(testReview.getReviewId());

      // then
      assertNotNull(response);
      assertEquals(testReview.getContents(), response.contents());
      then(reviewRepository).should(times(1))
          .findByReviewIdAndIsDeletedFalse(testReview.getReviewId());
    }
  }

  @Test
  @DisplayName("삭제된 리뷰 아이디로 조회하는 경우 단건 조회 실패 테스트")
  void readOne_Failure() {

    // given
    Review testReview = TestMockData.testReview();
    testReview.deleteReview(testReview.getReviewId());

    // then-then
    assertThrows(CustomReviewException.class, () -> {
      reviewService.readOne(testReview.getReviewId());
    });
  }

  @Nested
  public class searchReview {

    @Test
    @DisplayName("리뷰 검색 성공 테스트")
    void searchReview_Success() {

      // given
      Role role = Role.USER;
      ReviewSearchRequest request = mock(ReviewSearchRequest.class);
      Pageable pageable = PageRequest.of(0, 10);
      List<Review> reviews = List.of(TestMockData.testReview());
      Page<Review> reviewPage = new PageImpl<>(reviews);

      given(reviewRepository.searchReview(role, request, pageable)).willReturn(reviewPage);

      // when
      Page<ReviewGetResponse> responsePage = reviewService.searchReview(role, request, pageable);

      // then
      assertNotNull(responsePage);
      assertEquals(1, responsePage.getTotalElements());
      verify(reviewRepository, times(1)).searchReview(role, request, pageable);
    }
  }

  @Nested
  public class updateReview {

    @Test
    @DisplayName("리뷰 수정 성공 테스트")
    void updateReview_Success() {

      // given
      Review testReview = TestMockData.testReview();
      ReviewUpdateRequest request = new ReviewUpdateRequest("수정된 내용");
      given(reviewRepository.findByReviewIdAndIsDeletedFalse(anyString())).willReturn(
          Optional.of(testReview));

      // when
      reviewService.updateReview(testReview.getReviewId(), request, testReview.getUserId(),
          Role.USER);

      // then
      assertEquals(testReview.getContents(), request.contents());
    }
  }

  @Test
  @DisplayName("리뷰를 작성한 유저가 아닌 경우 실패 테스트")
  void updateReview_Failure() {

    // given
    String userId = "anotherUser";
    Review testReview = TestMockData.testReview();
    ReviewUpdateRequest request = new ReviewUpdateRequest("수정된 내용");
    given(reviewRepository.findByReviewIdAndIsDeletedFalse(anyString())).willReturn(
        Optional.of(testReview));

    // when-
    assertThrows(CustomReviewException.class, () -> {
      reviewService.updateReview(testReview.getReviewId(), request, userId, Role.USER);
    });
  }

  @Nested
  public class deleteReview {

    @Test
    @DisplayName("리뷰 삭제 성공 테스트")
    void deleteReview_Success() {

      // given
      Review testReview = TestMockData.testReview();
      given(reviewRepository.findByReviewIdAndIsDeletedFalse(anyString())).willReturn(
          Optional.of(testReview));

      // when
      reviewService.deleteReview(testReview.getReviewId(), testReview.getUserId(), Role.USER);

      // then
      assertTrue(testReview.isDeleted());
    }

    @Test
    @DisplayName("리뷰의 주인이 아닌 경우 리뷰 삭제 실패 테스트")
    void deleteReview_FailureAsUser() {
      // given
      String userId = "anotherUser";
      Review testReview = TestMockData.testReview();
      given(reviewRepository.findByReviewIdAndIsDeletedFalse(anyString())).willReturn(
          Optional.of(testReview));

      // when-then
      assertThrows(CustomReviewException.class, () -> {
        reviewService.deleteReview(testReview.getReviewId(), userId, Role.USER);
      });
    }
  }
}
