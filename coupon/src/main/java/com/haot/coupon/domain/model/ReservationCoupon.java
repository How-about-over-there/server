package com.haot.coupon.domain.model;

import com.haot.coupon.domain.model.enums.ReservationCouponStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_reservation_coupon")
public class ReservationCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_coupon_id")
    private UserCoupon userCoupon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "reservation_coupon_status")
    private ReservationCouponStatus reservationCouponStatus = ReservationCouponStatus.PREEMPTION;

    @Column(nullable = false, name = "reservation_price")
    private Double reservationPrice;

    @Column(nullable = false, name = "reservation_discount_price")
    private Double reservationDiscountPrice;

    @Column(nullable = false)
    private boolean isDelete = false;
}
