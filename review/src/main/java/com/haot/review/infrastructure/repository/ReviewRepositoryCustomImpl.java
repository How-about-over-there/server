package com.haot.review.infrastructure.repository;


import com.haot.review.application.dtos.req.ReviewSearchRequest;
import com.haot.review.domain.model.QReview;
import com.haot.review.domain.model.Review;
import com.haot.submodule.role.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
  QReview review = QReview.review;

  @Override
  public Page<Review> searchReview(Role role, ReviewSearchRequest request, Pageable pageable) {

    JPAQuery<Review> query = queryFactory.selectFrom(review)
        .where(conditions(role, request))
        .orderBy(getOrderSpecifiers(pageable))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    long total = Optional.ofNullable(queryFactory
            .select(review.count())
            .from(review)
            .where(conditions(role, request))
            .fetchOne())
        .orElseThrow();

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

  private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
    return pageable.getSort().stream()
        .map(order -> {
          String property = order.getProperty();
          boolean isAscending = order.isAscending();
          return switch (property) {
            case "createdAt" -> isAscending ? review.createdAt.asc() : review.createdAt.desc();
            case "updatedAt" -> isAscending ? review.updatedAt.asc() : review.updatedAt.desc();
            case "lodgeId" -> isAscending ? review.lodgeId.asc() : review.lodgeId.desc();
            case "userId" -> isAscending ? review.userId.asc() : review.userId.desc();
            default -> review.createdAt.asc();
          };
        })
        .toArray(OrderSpecifier<?>[]::new);
  }
}
