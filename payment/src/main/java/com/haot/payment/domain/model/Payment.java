package com.haot.payment.domain.model;

import com.haot.payment.domain.enums.PaymentMethod;
import com.haot.payment.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_payment", schema = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "reservation_id", nullable = false)
    private String reservationId;

    @Column(name = "imp_uid", nullable = false)
    private String impUid;

    @Column(name = "merchant_uid", nullable = false)
    private String merchantUid;

    @Column(name = "price", nullable = false)
    @Min(0)
    private Double price;

    @Column(name = "final_price", nullable = false)
    @Min(0)
    private Double finalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;
}
