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

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "price", nullable = false)
    @Min(0)
    private Double price;

    @Column(name = "final_price")
    @Min(0)
    private Double finalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    public static Payment create(String userId, String reservationId, Double price, PaymentMethod method, PaymentStatus status) {
        return Payment.builder()
                .userId(userId)
                .reservationId(reservationId)
                .price(price)
                .method(method)
                .status(status)
                .build();
    }
}
