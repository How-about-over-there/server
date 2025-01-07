package com.haot.lodge.domain.repository.impl;

import static com.haot.lodge.domain.model.QLodgeDate.lodgeDate;

import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.repository.LodgeDateCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LodgeDateCustomRepositoryImpl implements LodgeDateCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<LodgeDate> findAllLodgeDateByRange(
            Pageable pageable, Lodge lodge, LocalDate start, LocalDate end
    ) {
        BooleanBuilder booleanBuilder = getBooleanBuilder(lodge, start, end);
        List<LodgeDate> result = queryFactory.selectFrom(lodgeDate)
                .where(booleanBuilder)
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = queryFactory.select(lodgeDate.count())
                .from(lodgeDate)
                .where(booleanBuilder);
        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);
    }

    private BooleanBuilder getBooleanBuilder(
            Lodge lodge, LocalDate start, LocalDate end
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(eqLodge(lodge));
        builder.and(afterStart(start));
        builder.and(beforeEnd(end));
        builder.and(lodgeDate.isDeleted.eq(false));
        return builder;
    }

    private BooleanExpression eqLodge(Lodge linkedLodge) {
        return lodgeDate.lodge.eq(linkedLodge);
    }

    private BooleanExpression afterStart(LocalDate start) {
        return (start==null) ? null : lodgeDate.date.after(start.minusDays(1));
    }

    private BooleanExpression beforeEnd(LocalDate end) {
        return (end==null) ? null : lodgeDate.date.before(end.plusDays(1));
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return pageable.getSort().stream()
                .map(order -> {
                    ComparableExpressionBase<?> sortPath = getSortPath(order.getProperty());
                    return new OrderSpecifier<>(
                            order.isAscending()
                                    ? com.querydsl.core.types.Order.ASC
                                    : com.querydsl.core.types.Order.DESC,
                            sortPath);
                })
                .toArray(OrderSpecifier[]::new);
    }

    private ComparableExpressionBase<?> getSortPath(String property) {
        return switch (property) {
            case "date" -> lodgeDate.date;
            case "createdAt" -> lodgeDate.createdAt;
            case "updatedAt" -> lodgeDate.updatedAt;
            default -> throw new LodgeException(ErrorCode.UNSUPPORTED_SORT_TYPE);
        };
    }
}
