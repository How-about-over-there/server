package com.haot.reservation.application.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haot.reservation.domain.model.Reservation;
import com.haot.reservation.domain.model.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Schema(description = "예약 정보 응답 DTO")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ReservationGetResponse(
    @Schema(description = "예약 ID", example = "reservation123")
    String reservationId,

    @Schema(description = "사용자 ID", example = "user456")
    String userId,

    @Schema(description = "숙소 이름", example = "The Grand Lodge")
    String lodgeName,

    @Schema(description = "체크인 날짜", example = "2025-02-01")
    LocalDate checkInDate,

    @Schema(description = "체크아웃 날짜", example = "2025-02-05")
    LocalDate checkOutDate,

    @Schema(description = "투숙객 수", example = "2")
    Integer numGuests,

    @Schema(description = "사용자 요청사항", example = "조용한 방으로 부탁드립니다.")
    String request,

    @Schema(description = "총 가격", example = "150000.0")
    Double totalPrice,

    @Schema(description = "예약 상태", example = "COMPLETED")
    ReservationStatus status,

    @Schema(description = "결제 ID", example = "payment789")
    String paymentId,

    @Schema(description = "포인트 내역 ID", example = "pointHistory456")
    String pointHistoryId,

    @Schema(description = "상세 정보 URL", example = "https:/api/v1/payments/~")
    String url
) {

  public static ReservationGetResponse of(Reservation reservation, String url) {
    return ReservationGetResponse.builder()
        .reservationId(reservation.getReservationId())
        .userId(reservation.getUserId())
        .lodgeName(reservation.getLodgeName())
        .checkInDate(reservation.getCheckInDate())
        .checkOutDate(reservation.getCheckOutDate())
        .numGuests(reservation.getNumGuests())
        .request(reservation.getRequest())
        .totalPrice(reservation.getTotalPrice())
        .status(reservation.getStatus())
        .paymentId(reservation.getPaymentId())
        .pointHistoryId(reservation.getPointHistoryId())
        .url(url)
        .build();
  }
}
