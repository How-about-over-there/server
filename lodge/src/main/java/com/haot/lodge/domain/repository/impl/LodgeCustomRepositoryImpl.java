package com.haot.lodge.domain.repository.impl;

import static com.haot.lodge.domain.model.QLodge.lodge;
import static com.haot.lodge.domain.model.QLodgeDate.lodgeDate;
import static com.haot.lodge.domain.model.QLodgeRule.lodgeRule;
import static com.haot.lodge.domain.utils.QuerydslSortUtils.getOrderSpecifiers;


import com.haot.lodge.application.dto.LodgeSearchCriteria;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.domain.repository.LodgeCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class LodgeCustomRepositoryImpl implements LodgeCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "name", lodge.name,
            "price", lodge.basicPrice,
            "createdAt", lodge.createdAt,
            "updatedAt", lodge.updatedAt
    );

    @Override
    public Slice<Lodge> findAllByConditionOf(
            Pageable pageable, LodgeSearchCriteria searchCriteria
    ) {
        BooleanBuilder booleanBuilder = getBooleanBuilder(searchCriteria);
        List<Lodge> results = queryFactory.selectFrom(lodge)
                .join(lodgeRule).on(lodgeRule.lodge.eq(lodge)).fetchJoin()
                .join(lodgeDate).on(lodgeDate.lodge.eq(lodge)).fetchJoin()
                .distinct()
                .where(booleanBuilder)
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = queryFactory.select(lodge.count())
                .from(lodge)
                .where(booleanBuilder);
        return PageableExecutionUtils.getPage(results, pageable, count::fetchOne);
    }

    private BooleanBuilder getBooleanBuilder(LodgeSearchCriteria searchCriteria) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(likeName(searchCriteria.name()));
        builder.and(eqHostId(searchCriteria.hostId()));
        builder.and(likeAddress(searchCriteria.address()));
        builder.and(lodge.isDeleted.eq(false));
        builder.and(lodgeDate.isDeleted.eq(false));
        builder.and(geoMaxReservationDay(searchCriteria.maxReservationDay()));
        builder.and(geoMaxPersonnel(searchCriteria.maxPersonnel()));
        builder.and(isAvailableInDateRange(searchCriteria.checkInDate(), searchCriteria.checkOutDate()));
        return builder;
    }

    private BooleanExpression eqHostId(String hostId) {
        return hostId == null ? null : lodge.hostId.eq(hostId);
    }

    private BooleanExpression likeName(String lodgeName) {
        return lodgeName == null ? null : lodge.name.like("%" + lodgeName + "%");
    }

    private BooleanExpression likeAddress(String address) {
        return address == null ? null : lodge.address.like("%" + address + "%");
    }

    private BooleanExpression geoMaxPersonnel(Integer maxPersonnel) {
        return maxPersonnel == null ? null : lodgeRule.maxPersonnel.goe(maxPersonnel);
    }

    private BooleanExpression geoMaxReservationDay(Integer maxReservationDay) {
        return maxReservationDay == null ? null : lodgeRule.maxReservationDay.goe(maxReservationDay);
    }

    private BooleanExpression isAvailableInDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return null;
        }
        return lodge.id.in(
                queryFactory.select(lodgeDate.lodge.id)
                        .from(lodgeDate)
                        .where(lodgeDate.date
                                .between(checkInDate, checkOutDate)
                                .and(lodgeDate.status.eq(ReservationStatus.EMPTY)))
                        .groupBy(lodgeDate.lodge.id)
                        .having(lodgeDate.lodge
                                .count()
                                .eq(checkOutDate.toEpochDay() - checkInDate.toEpochDay() + 1))
        );
    }
}
