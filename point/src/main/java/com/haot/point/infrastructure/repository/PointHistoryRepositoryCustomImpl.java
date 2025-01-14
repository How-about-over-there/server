package com.haot.point.infrastructure.repository;

import com.haot.point.application.dto.request.history.PointHistorySearchRequest;
import com.haot.point.application.dto.response.PointHistoryResponse;
import com.haot.point.domain.enums.PointStatus;
import com.haot.point.domain.enums.PointType;
import com.haot.point.domain.model.PointHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import static com.haot.point.domain.model.QPointHistory.pointHistory;

@RequiredArgsConstructor
public class PointHistoryRepositoryCustomImpl implements PointHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PointHistoryResponse> searchPointHistories(PointHistorySearchRequest request, Pageable pageable) {
        // 조건 추가
        JPAQuery<PointHistory> query = queryFactory.selectFrom(pointHistory)
                .where(booleanBuilder(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(buildOrderSpecifier(pageable).toArray(new OrderSpecifier[0])); // 정렬 조건

        // 페이징 처리
        long total = query.fetchCount(); // 전체 데이터 수 계산

        List<PointHistoryResponse> results = query.fetch().stream().map(PointHistoryResponse::of).toList();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanBuilder booleanBuilder(PointHistorySearchRequest request) {

        return new BooleanBuilder()
                .and(isDeletedFalse())
                .and(eqUserId(request.getUserId()))
                .and(eqType(request.getType()))
                .and(filterEarnOrUse(request.getIsEarn(), request.getIsUse()))
                .and(eqStatus(request.getStatus()))
                .and(betweenPoint(request.getMinPoint(), request.getMaxPoint()))
                .and(betweenDates(request.getStart(), request.getEnd()));
    }

    // 삭제되지 않은 데이터 조건
    private BooleanExpression isDeletedFalse() {
        return pointHistory.isDeleted.isFalse();
    }

    // userId 조건
    private BooleanExpression eqUserId(String userId) {
        return userId != null ? pointHistory.userId.eq(userId) : null;
    }

    // 포인트 내역 타입 조건
    private BooleanExpression eqType(String type) {
        return type != null ? pointHistory.type.eq(PointType.fromString(type)) : null;
    }

    // 포인트 내역 적립/차감 구분 조건
    private BooleanExpression filterEarnOrUse(Boolean isEarn, Boolean isUse) {
        if (Boolean.TRUE.equals(isEarn)) {
            return pointHistory.type.in(PointType.EARN, PointType.CANCEL_USE);
        }
        if (Boolean.TRUE.equals(isUse)) {
            return pointHistory.type.in(PointType.USE, PointType.CANCEL_EARN, PointType.EXPIRE);
        }
        return null;
    }

    // 포인트 내역 상태 조건
    private BooleanExpression eqStatus(String status) {
        return status != null ? pointHistory.status.eq(PointStatus.fromString(status)) : null;
    }

    // 결제 가격 범위 조건
    private BooleanExpression betweenPoint(Double minPoint, Double maxPoint) {
        if (minPoint != null && maxPoint != null) {
            return pointHistory.points.between(minPoint, maxPoint);
        } else if (minPoint != null) {
            return pointHistory.points.goe(minPoint);
        } else if (maxPoint != null) {
            return pointHistory.points.loe(maxPoint);
        }
        return null;
    }

    // 생성일 범위 조건
    private BooleanExpression betweenDates(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return pointHistory.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59));
        } else if (start != null) {
            return pointHistory.createdAt.goe(start.atStartOfDay());
        } else if (end != null) {
            return pointHistory.createdAt.loe(end.atTime(23, 59, 59));
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
                case "type" -> orders.add(new OrderSpecifier<>(direction, pointHistory.type));
                case "status" -> orders.add(new OrderSpecifier<>(direction, pointHistory.status));
                case "points" -> orders.add(new OrderSpecifier<>(direction, pointHistory.points));
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, pointHistory.createdAt));
                default -> orders.add(new OrderSpecifier<>(com.querydsl.core.types.Order.ASC, pointHistory.createdAt)); // 기본 정렬
            }
        }
        return orders;
    }
}
