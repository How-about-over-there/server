package com.haot.reservation.application.dtos;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationData {

  private List<String> lodgeDateIds = new ArrayList<>();
  private Double totalPrice = 0.0;
  private String pointHistoryId = "NOT_APPLIED";
  private String reservationCouponId = "NOT_APPLIED";
  private String paymentId = "NOT_PROVIDED";
  private boolean couponApplied = false;
  private boolean pointApplied = false;

  public static ReservationData createWithLodgeData(List<String> lodgeDateIds, Double lodgePrice) {
    ReservationData reservationData = new ReservationData();
    reservationData.lodgeDateIds = lodgeDateIds;
    reservationData.totalPrice = lodgePrice;
    return reservationData;
  }
  public void applyCoupon(String couponId, Double applyCouponToPrice) {
    this.reservationCouponId = couponId;
    this.totalPrice = applyCouponToPrice;
    this.couponApplied = true;
  }

  public void applyPoint(String pointHistoryId, Double pointAmount) {
    this.pointHistoryId = pointHistoryId;
    this.totalPrice -= pointAmount;
    this.pointApplied = true;
  }
}
