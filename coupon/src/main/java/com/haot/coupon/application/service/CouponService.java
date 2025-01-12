package com.haot.coupon.application.service;

import com.haot.coupon.application.dto.feign.request.FeignConfirmReservationRequest;
import com.haot.coupon.application.dto.feign.request.FeignVerifyRequest;
import com.haot.coupon.application.dto.request.coupons.CouponCustomerCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponReadMeResponse;
import com.haot.coupon.application.dto.response.coupons.CouponSearchResponse;
import com.haot.coupon.application.dto.feign.response.ReservationVerifyResponse;
import com.haot.coupon.domain.model.Coupon;
import com.haot.coupon.domain.model.enums.EventStatus;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    void customerIssueCoupon(CouponCustomerCreateRequest request, String userId);

    void updateEndEventStatus(String eventId, EventStatus newStatus);

    CouponSearchResponse getCouponDetails(String couponId);

    ReservationVerifyResponse verify(FeignVerifyRequest request);

    void confirmReservation(String reservationCouponId, FeignConfirmReservationRequest request);

    Page<CouponReadMeResponse> getMyCoupons(String userId, Pageable pageable);

    void rollbackReservationCoupon(String userId, Role role, String reservationCouponId);

    void issuePriorityCoupon(String userId, CouponCustomerCreateRequest request);
}
