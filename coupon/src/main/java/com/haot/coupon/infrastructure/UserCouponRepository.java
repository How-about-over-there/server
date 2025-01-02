package com.haot.coupon.infrastructure;

import com.haot.coupon.domain.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, String> {
}
