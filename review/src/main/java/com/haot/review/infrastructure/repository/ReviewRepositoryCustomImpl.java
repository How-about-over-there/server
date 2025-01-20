package com.haot.review.infrastructure.repository;

import static com.haot.review.domain.model.QReview.review;
import static com.haot.review.infrastructure.utils.QuerydslSortUtils.getOrderSpecifiers;

import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.domain.model.Review;
import com.haot.submodule.role.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
      "userId", review.userId,
      "lodgeId", review.lodgeId,
      "createdAt", review.createdAt,
      "updatedAt", review.updatedAt
  );

  @Override
  public Page<Review> searchReview(Role role, ReviewSearchRequest request, Pageable pageable) {

    JPAQuery<Review> query = queryFactory.selectFrom(review)
        .where(conditions(role, request))
        .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    long total = Optional.ofNullable(queryFactory
            .select(review.count())
            .from(review)
            .where(conditions(role, request))
            .fetchOne())
        .orElse(0L);

    List<Review> content = query.stream().toList();

    return new PageImpl<>(content, pageable, total);
  }


  private BooleanBuilder conditions(Role role, ReviewSearchRequest request) {
    BooleanBuilder builder = new BooleanBuilder()
        .and(lodgeIdEq(request.lodgeId()))
        .and(userIdEq(request.userId()));

    if (Role.ADMIN.equals(role) || Role.HOST.equals(role)) {
      builder.and(deletedEq(request.isDeleted()));
    } else {
      builder.and(review.isDeleted.isFalse());
    }
    return builder;
  }

  private BooleanExpression lodgeIdEq(String lodgeId) {
    return lodgeId != null ? review.lodgeId.eq(lodgeId) : null;
  }

  private BooleanExpression userIdEq(String userId) {
    return userId != null ? review.userId.eq(userId) : null;
  }

  private BooleanExpression deletedEq(Boolean isDeleted) {
    return isDeleted != null ? review.isDeleted.eq(isDeleted) : null;
  }
}
