package com.haot.coupon.domain.model;

import com.haot.coupon.domain.model.enums.CouponStatus;
import com.haot.coupon.submodule.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_user_coupon", schema = "coupon")
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "coupon_status")
    private CouponStatus couponStatus;

    @Column(nullable = true, name = "used_date")
    private LocalDateTime usedDate;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete;
}
