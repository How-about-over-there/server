package com.haot.reservation.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_reservation")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "reservation_id", updatable = false, nullable = false)
  private String reservationId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "lodge_name", nullable = false)
  private String lodgeName;

  @Column(name = "check_in_date", nullable = false)
  private LocalDate checkInDate;

  @Column(name = "check_out_date", nullable = false)
  private LocalDate checkOutDate;

  @Column(name = "num_guests", nullable = false)
  private Integer numGuests;

  @Column(name = "request", nullable = false)
  private String request;

  @Column(name = "total_price", nullable = false)
  private Integer totalPrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "reservation_status", nullable = false)
  private ReservationStatus status;

  @Column(name = "point_id", nullable = false)
  private String pointId;

  @Column(name = "payment_id", nullable = false)
  private String paymentId;
  // null 로 있다가 생성되면 값이 넣어짐
  @Column(name = "reservation_coupon_id", nullable = true)
  private String reservationCouponId;
}
