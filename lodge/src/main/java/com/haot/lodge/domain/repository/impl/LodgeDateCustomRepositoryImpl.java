package com.haot.lodge.domain.repository.impl;

import static com.haot.lodge.domain.model.QLodgeDate.lodgeDate;
import static com.haot.lodge.domain.utils.QuerydslSortUtils.getOrderSpecifiers;

import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.repository.LodgeDateCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LodgeDateCustomRepositoryImpl implements LodgeDateCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "date", lodgeDate.date,
            "createdAt", lodgeDate.createdAt,
            "updatedAt", lodgeDate.updatedAt
    );

    @Override
    public boolean existsOverlappingDates(
            String lodgeId, LocalDate startDate, LocalDate endDate
    ) {
        return queryFactory
                .selectOne()
                .from(lodgeDate)
                .where(
                        lodgeDate.lodge.id.eq(lodgeId),
                        lodgeDate.date.between(startDate, endDate)
                )
                .fetchFirst() != null;
    }

    @Override
    public Slice<LodgeDate> findAllLodgeDateByRange(
            Pageable pageable, Lodge lodge, LocalDate start, LocalDate end
    ) {
        BooleanBuilder booleanBuilder = getBooleanBuilder(lodge, start, end);
        List<LodgeDate> result = queryFactory.selectFrom(lodgeDate)
                .where(booleanBuilder)
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
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

}
