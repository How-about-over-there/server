package com.haot.coupon.domain.model;

import com.haot.coupon.domain.model.enums.ReservationCouponStatus;
import com.haot.submodule.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_reservation_coupon", schema = "coupon")
public class ReservationCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_coupon_id")
    private UserCoupon userCoupon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "reservation_coupon_status")
    private ReservationCouponStatus reservationCouponStatus;

    @Column(nullable = false, name = "reservation_price")
    private Double reservationPrice;

    @Column(nullable = false, name = "reservation_discount_price")
    private Double reservationDiscountPrice;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete;
}
