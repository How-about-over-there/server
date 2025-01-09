package com.haot.coupon.infrastructure.repository.impl;

import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import com.haot.coupon.application.mapper.UserCouponMapper;
import com.haot.coupon.domain.model.QCoupon;
import com.haot.coupon.domain.model.QUserCoupon;
import com.haot.coupon.domain.model.UserCoupon;
import com.haot.coupon.infrastructure.repository.UserCouponCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class UserCouponCustomRepositoryImpl implements UserCouponCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final UserCouponMapper userCouponMapper;

    QCoupon coupon = QCoupon.coupon;
    QUserCoupon userCoupon = QUserCoupon.userCoupon;

    @Override
    public Page<CouponReadMeResponse> checkMyCouponBox(String userId, Pageable pageable) {

        BooleanBuilder booleanBuilder = getBooleanBuilder(userId);
        // 엔티티 조회
        List<UserCoupon> userCoupons = queryFactory
                .selectFrom(userCoupon)
                .join(userCoupon.coupon, coupon).fetchJoin()
                .where(booleanBuilder)
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
        return builder;
    }
}
