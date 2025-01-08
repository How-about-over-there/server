package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, String> {
    boolean existsByUserIdAndCouponId(String userId, String couponId);

    Optional<UserCoupon> findByUserIdAndCouponId(String userId, String couponId);
}
