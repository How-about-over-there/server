package com.haot.coupon.presentation.docs;

import com.haot.coupon.application.dto.request.coupons.CouponCreateRequest;
import com.haot.coupon.application.dto.response.coupons.CouponCreateResponse;
import com.haot.coupon.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin 쿠폰 API Controller", description = "Role이 Admin만 사용할 수 있는 쿠폰 API 목록입니다.")
public interface AdminCouponControllerDocs {

    @Operation(summary = "쿠폰 생성 API", description = "관리자 쿠폰 생성 API 입니다.")
    ApiResponse<CouponCreateResponse> create(CouponCreateRequest request);

    ApiResponse<Void> delete(String couponId);

    ApiResponse<Void> deleteCouponHistory(String reservationCouponId);
}
