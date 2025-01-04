package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    Optional<Coupon> findByIdAndExpiredDateIsAfter(String couponId, LocalDateTime eventEndDate);
}
