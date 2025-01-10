package com.haot.reservation.domain.model;

import com.haot.submodule.auditor.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class Reservation extends BaseEntity {

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
  private Double totalPrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "reservation_status", nullable = false)
  private ReservationStatus status;

  @Column(name = "point_history_id", nullable = true)
  private String pointHistoryId;

  @Column(name = "payment_id", nullable = true)
  private String paymentId;

  @Column(name = "reservation_coupon_id", nullable = true)
  private String reservationCouponId;

  @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ReservationDate> dates = new ArrayList<>();

  public static Reservation createReservation(
      String userId,
      String lodgeName,
      LocalDate checkInDate,
      LocalDate checkOutDate,
      Integer numGuests,
      String request,
      Double totalPrice,
      String paymentId,
      String reservationCouponId,
      String pointHistoryId
  ) {
    return Reservation.builder()
        .userId(userId)
        .lodgeName(lodgeName)
        .checkInDate(checkInDate)
        .checkOutDate(checkOutDate)
        .numGuests(numGuests)
        .request(request)
        .totalPrice(totalPrice)
        .status(ReservationStatus.PENDING)
        .paymentId(paymentId)
        .reservationCouponId(reservationCouponId)
        .pointHistoryId(pointHistoryId)
        .build();
  }
}
