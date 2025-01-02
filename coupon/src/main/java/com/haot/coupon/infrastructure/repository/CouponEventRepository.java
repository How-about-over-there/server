package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponEventRepository extends JpaRepository<CouponEvent, String> {
}
