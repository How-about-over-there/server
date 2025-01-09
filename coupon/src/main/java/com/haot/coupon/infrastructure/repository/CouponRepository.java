package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    Optional<Coupon> findByIdAndExpiredDateIsAfterAndIsDeleteFalse(String couponId, LocalDateTime eventEndDate);

    Optional<Coupon> findByIdAndIsDeleteFalse(String couponId);

    @Modifying
    @Query(value = "update coupon.p_coupon SET issued_quantity = issued_quantity + 1 WHERE id = :id", nativeQuery = true)
    int increaseIssuedQuantity(@Param("id") String id);
}
