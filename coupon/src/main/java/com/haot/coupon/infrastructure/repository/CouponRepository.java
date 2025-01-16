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
    @Query(value = "update coupon.p_coupon SET issued_quantity = issued_quantity + 1 WHERE id = :id", nativeQuery = true)
    int increaseIssuedQuantity(@Param("id") String id);

    @Modifying
    @Query(value = "update coupon.p_coupon SET issued_quantity = issued_quantity + :issuedCount WHERE id = :id", nativeQuery = true)
    void increaseBatchIssuedQuantity(@Param("id") String id, @Param("issuedCount") Integer issuedCount);

    // userid와 couponid가 맞는 userCoupon테이블에 있으면 null return, 없으면 coupon return
    @Query(value = """
        SELECT c.* 
        FROM coupon.p_coupon c 
        WHERE c.id = :couponId 
          AND c.is_delete = false 
          AND NOT EXISTS (
              SELECT 1 
              FROM coupon.p_user_coupon uc 
              WHERE uc.coupon_id = c.id 
                AND uc.user_id = :userId 
                AND uc.is_delete = false
        )
    """, nativeQuery = true)
    Optional<Coupon> findCouponIfNoUserCoupon(@Param("couponId") String couponId, @Param("userId") String userId);

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
