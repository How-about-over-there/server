package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.model.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponEventRepository extends JpaRepository<CouponEvent, String>, CouponEventCustomRepository {

    boolean existsByCouponIdAndIsDeleteFalse(String CouponId);

    List<CouponEvent> findByCouponIdAndEventEndDateIsAfterAndIsDeleteFalse(String id, LocalDateTime now);

    @Query("SELECT ce FROM CouponEvent ce JOIN FETCH ce.coupon c " +
            "WHERE ce.id = :eventId AND ce.eventStatus = :eventStatus " +
            "AND ce.isDelete = false AND c.isDelete = false")
    Optional<CouponEvent> findByIdAndEventStatusAndIsDeleteFalse(@Param("eventId") String eventId,
                                                                 @Param("eventStatus") EventStatus eventStatus);

    Optional<CouponEvent> findByIdAndIsDeleteFalse(String eventId);
}
