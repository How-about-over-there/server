package com.haot.coupon.infrastructure.repository;

import com.haot.coupon.domain.model.ReservationCoupon;
import com.haot.coupon.domain.model.UserCoupon;
import com.haot.coupon.domain.model.enums.ReservationCouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationCouponRepository extends JpaRepository<ReservationCoupon, String> {
    boolean existsByUserCouponAndReservationCouponStatusNotInAndIsDeletedFalse(
            UserCoupon userCoupon, List<ReservationCouponStatus> excludedStatuses);
}
