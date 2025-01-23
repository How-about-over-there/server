package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    Optional<Coupon> findByIdAndExpiredDateIsAfterAndIsDeleteFalse(String couponId, LocalDateTime eventEndDate);

    Optional<Coupon> findByIdAndIsDeleteFalse(String couponId);

    @Modifying
    @Query(value = "update coupon.p_coupon SET issued_quantity = issued_quantity + :issuedCount WHERE id = :id", nativeQuery = true)
    void increaseBatchIssuedQuantity(@Param("id") String id, @Param("issuedCount") Integer issuedCount);

    @Query(value = """
        SELECT uc.user_id 
        FROM coupon.p_coupon c
        JOIN coupon.p_user_coupon uc
            ON c.id = uc.coupon_id
        WHERE uc.coupon_id = :couponId 
          AND uc.user_id IN :userIds
    """, nativeQuery = true)
    Set<String> findUserIdHavingCoupon(@Param("couponId") String couponId, @Param("userIds") List<String> userIds);

}
