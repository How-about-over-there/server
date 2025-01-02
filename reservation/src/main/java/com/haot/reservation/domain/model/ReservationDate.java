package com.haot.reservation.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "p_reservation_date")
public class ReservationDate {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "reservation_date_id", updatable = false, nullable = false)
  private String reservationDateId;

  @Column(name = "reservation_id", nullable = false)
  private String reservationId;

  @Column(name = "date_id", nullable = false)
  private String dateId;
}
