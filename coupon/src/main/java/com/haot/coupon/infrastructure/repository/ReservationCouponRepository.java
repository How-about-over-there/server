package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.ReservationCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationCouponRepository extends JpaRepository<ReservationCoupon, String> {
}
