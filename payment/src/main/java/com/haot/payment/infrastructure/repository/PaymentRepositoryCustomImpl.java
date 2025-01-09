package com.haot.payment.infrastructure.repository;

import com.haot.payment.application.dto.response.PaymentResponse;
import com.haot.payment.common.exception.CustomPaymentException;
import com.haot.payment.common.exception.enums.ErrorCode;
import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import com.haot.payment.domain.model.Payment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.haot.payment.domain.model.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PaymentResponse> searchPayments(String userId, String reservationId, String merchantId,
                                                PaymentMethod method, PaymentStatus status,
                                                Double minPrice, Double maxPrice,
                                                LocalDate start, LocalDate end, Pageable pageable) {

        // 조건 추가
        JPAQuery<Payment> query = queryFactory.selectFrom(payment)
                .where(
                        isDeletedFalse(),
                        eqUserId(userId),
                        eqReservationId(reservationId),
                        eqMerchantId(merchantId),
                        eqMethod(method),
                        eqStatus(status),
                        betweenPrice(minPrice, maxPrice),
                        betweenDates(start, end)
                );

        // 동적 정렬
        List<OrderSpecifier<?>> orderSpecifier = buildOrderSpecifier(pageable);

        // 페이징 처리
        long total = query.fetchCount(); // 전체 데이터 수 계산
        List<Payment> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier.toArray(new OrderSpecifier[0])) // 정렬 조건
                .fetch();

        return new PageImpl<>(results.stream().map(PaymentResponse::of).toList(), pageable, total);
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
    private BooleanExpression eqMethod(PaymentMethod method) {
        return method != null ? payment.method.eq(method) : null;
    }

    // 결제 상태 조건
    private BooleanExpression eqStatus(PaymentStatus status) {
        return status != null ? payment.status.eq(status) : null;
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

    // 동적 정렬 조건 생성
    private List<OrderSpecifier<?>> buildOrderSpecifier(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            // 정렬 방향 결정 (ASC/DESC)
            com.querydsl.core.types.Order direction = sortOrder.isAscending()
                    ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;

            // 정렬 필드에 따른 OrderSpecifier 추가
            switch (sortOrder.getProperty()) {
                case "method":
                    orders.add(new OrderSpecifier<>(direction, payment.method));
                    break;
                case "status":
                    orders.add(new OrderSpecifier<>(direction, payment.status));
                    break;
                case "price":
                    orders.add(new OrderSpecifier<>(direction, payment.price));
                    break;
                case "createdAt":
                    orders.add(new OrderSpecifier<>(direction, payment.createdAt));
                    break;
                default:
                    throw new CustomPaymentException(ErrorCode.INVALID_SORT_EXCEPTION);
            }
        }
        return orders;
    }
}
