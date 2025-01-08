package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.CouponEvent;
import com.haot.coupon.domain.model.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponEventRepository extends JpaRepository<CouponEvent, String> {

    boolean existsByCouponIdAndIsDeleteFalse(String CouponId);

    List<CouponEvent> findByCouponIdAndEventEndDateIsAfterAndIsDeleteFalse(String id, LocalDateTime now);

    Optional<CouponEvent> findByIdAndEventStatusAndIsDeleteFalse(String id, EventStatus eventStatus);
}
