package com.haot.coupon.infrastructure.repository.impl;

import com.haot.coupon.application.dto.request.events.EventSearchRequest;
import com.haot.coupon.application.dto.response.events.EventSearchResponse;
import com.haot.coupon.application.mapper.EventMapper;
import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.model.enums.EventStatus;
import com.haot.coupon.infrastructure.repository.CouponEventCustomRepository;
import com.haot.submodule.role.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.haot.coupon.domain.model.QCouponEvent.couponEvent;
import static com.haot.coupon.domain.utils.QueryDslSortUtils.getOrderSpecifiers;

@RequiredArgsConstructor
public class CouponEventCustomRepositoryImpl implements CouponEventCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final EventMapper eventMapper;

    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "eventStartDate", couponEvent.eventStartDate,
            "eventEndDate", couponEvent.eventEndDate,
            "createdAt", couponEvent.createdAt,
            "updatedAt", couponEvent.updatedAt
    );

    @Override
    public Page<EventSearchResponse> searchEventByRole(Role userRole, EventSearchRequest request, Pageable pageable) {

        BooleanBuilder booleanBuilder = getBooleanBuilder(userRole, request);

        List<CouponEvent> couponEvents = queryFactory
                .selectFrom(couponEvent)
                .where(booleanBuilder)
                .where(booleanBuilder)
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<EventSearchResponse> results = couponEvents.stream()
                .map(eventMapper::toEventResponseDTO)
                .toList();

        Long total = Objects.requireNonNullElse(
                queryFactory
                        .select(couponEvent.count())
                        .from(couponEvent)
                        .where(booleanBuilder)
                        .fetchOne(),
                0L)
                ;

        return new PageImpl<>(results, pageable, total);

    }

    private BooleanBuilder getBooleanBuilder(Role userRole, EventSearchRequest request) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(likeEventName(request.getNameKeyword()));
        builder.and(likeEventDescription(request.getDescriptionKeyword()));
        builder.and(checkIsDeleted(userRole, request.getIsDelete()));
        builder.and(searchToEventStatus(userRole, request.getEventStatus()));
        builder.and(searchToDate(userRole, request.getStartDate(), request.getEndDate()));
        return builder;
    }

    private boolean roleCheckIfUser(Role userRole) {
        return userRole == Role.USER;
    }

    private BooleanExpression searchToDate(Role userRole, LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder builder = new BooleanBuilder();

        if(roleCheckIfUser(userRole)) {
            LocalDateTime now = LocalDateTime.now();

            builder.and(couponEvent.eventEndDate.after(now).and(couponEvent.eventStartDate.before(now)));
        }

        if (startDate != null) {
            builder.and(couponEvent.eventStartDate.after(startDate));
        }
        if (endDate != null) {
            builder.and(couponEvent.eventEndDate.before(endDate));
        }

        // 강제 형변환 포함하여 BooleanExpression 반환
        return builder.hasValue() ? (BooleanExpression) builder.getValue() : null;
    }

    // 대소문자 구별없이 like
    private BooleanExpression likeEventName(String eventName) {
        return StringUtils.hasText(eventName) ? couponEvent.eventName.containsIgnoreCase(eventName.trim()) : null;
    }

    private BooleanExpression likeEventDescription(String eventDescription) {
        return StringUtils.hasText(eventDescription) ? couponEvent.eventName.containsIgnoreCase(eventDescription.trim()) : null;
    }

    private BooleanExpression checkIsDeleted(Role userRole, Boolean isDeleted) {

        if(roleCheckIfUser(userRole)){
            return couponEvent.isDeleted.eq(false);
        }

        return isDeleted == null ? null : couponEvent.isDeleted.eq(isDeleted);
    }

    private BooleanExpression searchToEventStatus(Role userRole, String eventStatus) {

        if (roleCheckIfUser(userRole)) {
            return couponEvent.eventStatus.eq(EventStatus.DEFAULT);
        }

        return StringUtils.hasText(eventStatus) ? couponEvent.eventStatus.eq(EventStatus.checkEventStatus(eventStatus)) : null;
    }


}
