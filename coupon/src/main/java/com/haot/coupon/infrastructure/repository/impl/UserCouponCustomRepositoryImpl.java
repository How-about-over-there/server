package com.haot.coupon.infrastructure.repository.impl;

import static com.haot.coupon.domain.model.QUserCoupon.userCoupon;
import static com.haot.coupon.domain.model.QCoupon.coupon;
import static com.haot.coupon.domain.utils.QueryDslSortUtils.getOrderSpecifiers;

import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import com.haot.coupon.application.mapper.UserCouponMapper;
import com.haot.coupon.domain.model.UserCoupon;
import com.haot.coupon.domain.model.enums.CouponStatus;
import com.haot.coupon.infrastructure.repository.UserCouponCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class UserCouponCustomRepositoryImpl implements UserCouponCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final UserCouponMapper userCouponMapper;

    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "availableDate", coupon.availableDate,
            "expiredDate", coupon.expiredDate,
            "createdAt", userCoupon.createdAt,
            "updatedAt", userCoupon.updatedAt
    );

    @Override
    public Page<CouponReadMeResponse> checkMyCouponBox(String userId, Pageable pageable) {

        BooleanBuilder booleanBuilder = getBooleanBuilder(userId);
        // 엔티티 조회
        List<UserCoupon> userCoupons = queryFactory
                .selectFrom(userCoupon)
                .join(userCoupon.coupon, coupon).fetchJoin()
                .where(booleanBuilder)
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // DTO로 변환
        List<CouponReadMeResponse> results = userCoupons.stream()
                .map(userCouponMapper::toCouponReadMeResponse)
                .toList();

        // 총 개수 계산
        Long total = Objects.requireNonNullElse(
                queryFactory
                        .select(userCoupon.count())
                        .from(userCoupon)
                        .join(userCoupon.coupon, coupon)
                        .where(booleanBuilder)
                        .fetchOne(),
                0L)
                ;

        return new PageImpl<>(results, pageable, total);

    }

    private BooleanBuilder getBooleanBuilder(String userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(userCoupon.userId.eq(userId));
        builder.and(coupon.isDeleted.eq(false));
        builder.and(userCoupon.isDeleted.eq(false));
        builder.and(userCoupon.couponStatus.ne(CouponStatus.USED));
        builder.and(userCoupon.usedDate.isNull());
        return builder;
    }
}
