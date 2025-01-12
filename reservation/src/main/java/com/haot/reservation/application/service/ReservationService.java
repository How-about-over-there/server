package com.haot.reservation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.reservation.application.dtos.req.ReservationCreateRequest;
import com.haot.reservation.application.dtos.req.ReservationUpdateRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.submodule.role.Role;

public interface ReservationService {

  /**
   * 예약 생성
   * 예약 생성시 쿠폰, 포인트를 적용할 수 있습니다(request에 해당 값이 있는 경우 적용)
   * @param request 예약 생성 데이터
   * @param userId 예약 생성 요청한 유저 아이디
   * @param role USER
   * @return 예약 정보 및 결제 url
   * @throws JsonProcessingException feignClient(상대방 서버의) 실패 응답
   */
  ReservationGetResponse createReservation(
      ReservationCreateRequest request,
      String userId,
      Role role
  ) throws JsonProcessingException;

  /**
   * 예약 단건 조회
   * @param reservationId 예약 아이디
   * @param userId 유저 아이디
   * @param role USER
   * @return 예약 내역
   */
  ReservationGetResponse getReservation(
      String reservationId,
      String userId,
      Role role
  );

  /**
   * 예약 수정 (결제 완료 or 취소)
   * 결제 완료시 숙소, 쿠폰, 포인트의 상태를 변경합니다.
   * @param reservationUpdateRequest paymentId, 예약 상태 COMPLETED or CANCELED
   * @param reservationId 예약 아이디
   * @param userId 유저 아이디
   * @param role 권한
   * @throws JsonProcessingException feignClient(상대방 서버의) 실패 응답
   */
  void updateReservation(
      ReservationUpdateRequest reservationUpdateRequest,
      String reservationId,
      String userId,
      Role role
  ) throws JsonProcessingException;
}
