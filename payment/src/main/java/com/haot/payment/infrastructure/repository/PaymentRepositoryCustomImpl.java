package com.haot.payment.infrastructure.repository;

import com.haot.payment.application.dto.request.PaymentSearchRequest;
import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import com.haot.payment.domain.model.Payment;
import com.haot.payment.domain.utils.QueryDslSortUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.haot.payment.domain.model.QPayment.payment;
import static com.haot.payment.domain.utils.QueryDslSortUtils.getOrderSpecifiers;

@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    // 동적 정렬 조건 생성
    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
                "method", payment.method,
                "status", payment.status,
                "price", payment.price,
                "createdAt", payment.createdAt
    );

    @Override
    public Page<PaymentResponse> searchPayments(PaymentSearchRequest request, Pageable pageable) {

        // 조건 추가
        JPAQuery<Payment> query = queryFactory.selectFrom(payment)
                .where(booleanBuilder(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS)); // 정렬 조건

        // 페이징 처리
        long total = query.fetchCount(); // 전체 데이터 수 계산

        List<PaymentResponse> results = query.fetch().stream().map(PaymentResponse::of).toList();

        return new PageImpl<>(results, pageable, total);
    }


    private BooleanBuilder booleanBuilder(PaymentSearchRequest request) {

        return new BooleanBuilder()
                .and(isDeletedFalse())
                .and(eqUserId(request.getUserId()))
                .and(eqReservationId(request.getReservationId()))
                .and(eqMerchantId(request.getMerchantId()))
                .and(eqMethod(request.getMethod()))
                .and(eqStatus(request.getStatus()))
                .and(betweenPrice(request.getMinPrice(), request.getMaxPrice()))
                .and(betweenDates(request.getStart(), request.getEnd()));
    }

    // 삭제되지 않은 데이터 조건
    private BooleanExpression isDeletedFalse() {
        return payment.isDeleted.isFalse();
    }

    // userId 조건
    private BooleanExpression eqUserId(String userId) {
        return userId != null ? payment.userId.eq(userId) : null;
    }

    // reservationId 조건
    private BooleanExpression eqReservationId(String reservationId) {
        return reservationId != null ? payment.reservationId.eq(reservationId) : null;
    }

    // merchantId 조건
    private BooleanExpression eqMerchantId(String merchantId) {
        return merchantId != null ? payment.merchantId.eq(merchantId) : null;
    }

    // 결제 방식 조건
    private BooleanExpression eqMethod(String method) {
        return method != null ? payment.method.eq(PaymentMethod.fromString(method)) : null;
    }

    // 결제 상태 조건
    private BooleanExpression eqStatus(String status) {
        return status != null ? payment.status.eq(PaymentStatus.fromString(status)) : null;
    }

    // 결제 가격 범위 조건
    private BooleanExpression betweenPrice(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return payment.price.between(minPrice, maxPrice);
        } else if (minPrice != null) {
            return payment.price.goe(minPrice);
        } else if (maxPrice != null) {
            return payment.price.loe(maxPrice);
        }
        return null;
    }

    // 생성일 범위 조건
    private BooleanExpression betweenDates(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return payment.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59));
        } else if (start != null) {
            return payment.createdAt.goe(start.atStartOfDay());
        } else if (end != null) {
            return payment.createdAt.loe(end.atTime(23, 59, 59));
        }
        return null;
    }
}
