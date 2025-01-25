package com.haot.reservation.infrastructure.repository;

import static com.haot.reservation.domain.model.QReservation.reservation;
import static com.haot.reservation.domain.utils.QuerydslSortUtils.getOrderSpecifiers;

import com.haot.reservation.application.dto.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dto.req.ReservationSearchRequest;
import com.haot.reservation.domain.model.Reservation;
import com.haot.reservation.domain.model.ReservationStatus;
import com.haot.submodule.role.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

  private final JPAQueryFactory queryFactory;

  private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
      "totalPrice", reservation.totalPrice,
      "createdAt", reservation.createdAt,
      "updatedAt", reservation.updatedAt
  );

  @Override
  public Page<Reservation> searchReservation(
      ReservationSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  ) {

    JPAQuery<Reservation> query = queryFactory.selectFrom(reservation)
        .where(conditions(request, userId))
        .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    List<Reservation> content = query.fetch();

    long total = Optional.ofNullable(queryFactory.select(reservation.count())
            .from(reservation)
            .where(conditions(request, userId))
            .fetchOne())
        .orElse(0L);

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public Page<Reservation> search(
      ReservationAdminSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  ) {
    JPAQuery<Reservation> query = queryFactory.selectFrom(reservation)
        .where(adminConditions(request))
        .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    List<Reservation> content = query.fetch();

    long total = Optional.ofNullable(queryFactory.select(reservation.count())
            .from(reservation)
            .where(adminConditions(request))
            .fetchOne())
        .orElse(0L);

    return new PageImpl<>(content, pageable, total);
  }

  private BooleanBuilder conditions(ReservationSearchRequest request, String userId) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.and(userIdEq(userId));
    booleanBuilder.and(checkInDate(request.checkInDate()));
    booleanBuilder.and(checkOutDate(request.checkOutDate()));
    booleanBuilder.and(reservationStatusEq(request.reservationStatus()));

    return booleanBuilder;
  }

  private BooleanBuilder adminConditions(ReservationAdminSearchRequest request) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.and(userIdEq(request.userId()));
    booleanBuilder.and(reservationIdEq(request.reservationId()));
    booleanBuilder.and(checkInDate(request.checkInDate()));
    booleanBuilder.and(checkOutDate(request.checkOutDate()));
    booleanBuilder.and(reservationStatusEq(request.reservationStatus()));

    return booleanBuilder;
  }

  private BooleanExpression reservationIdEq(String reservationId) {
    return (reservationId == null) ? null : reservation.reservationId.eq(reservationId);
  }

  private BooleanExpression userIdEq(String userId) {
    return (userId == null) ? null : reservation.userId.eq(userId);
  }

  private BooleanExpression checkInDate(LocalDate checkInDate) {
    return (checkInDate == null) ? null : reservation.checkInDate.goe(checkInDate);
  }

  private BooleanExpression checkOutDate(LocalDate checkOutDate) {
    return (checkOutDate == null) ? null : reservation.checkOutDate.loe(checkOutDate);
  }

  private BooleanExpression reservationStatusEq(String status) {
    return status != null ? reservation.status.eq(ReservationStatus.valueOf(status)) : null;
  }
}
