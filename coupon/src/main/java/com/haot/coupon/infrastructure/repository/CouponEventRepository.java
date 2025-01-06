package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponEventRepository extends JpaRepository<CouponEvent, String> {

    boolean existsByCouponIdAndIsDeleteFalse(String CouponId);

    List<CouponEvent> findByCouponIdAndEventEndDateIsAfterAndIsDeleteFalse(String id, LocalDateTime now);
}
