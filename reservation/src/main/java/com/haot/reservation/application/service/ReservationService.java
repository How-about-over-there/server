package com.haot.reservation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haot.reservation.application.dtos.req.ReservationCreateRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.submodule.role.Role;
import org.springframework.transaction.annotation.Transactional;

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
}
