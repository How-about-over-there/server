package com.haot.reservation.application.service;

import com.haot.reservation.application.dtos.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dtos.req.ReservationCancelRequest;
import com.haot.reservation.application.dtos.req.ReservationCreateRequest;
import com.haot.reservation.application.dtos.req.ReservationSearchRequest;
import com.haot.reservation.application.dtos.req.ReservationUpdateRequest;
import com.haot.reservation.application.dtos.res.ReservationGetResponse;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationService {

  /**
   * 예약 생성 예약 생성시 쿠폰, 포인트를 적용할 수 있습니다(request에 해당 값이 있는 경우 적용)
   *
   * @param request 예약 생성 데이터
   * @param userId  예약 생성 요청한 유저 아이디
   * @param role    USER
   * @return 예약 정보 및 결제 url
   */
  ReservationGetResponse createReservation(
      ReservationCreateRequest request,
      String userId,
      Role role
  );

  /**
   * 예약 단건 조회
   *
   * @param reservationId 예약 아이디
   * @param userId        유저 아이디
   * @param role          USER
   * @return 예약 내역
   */
  ReservationGetResponse getReservation(
      String reservationId,
      String userId,
      Role role
  );

  /**
   * 일반 유저 예약 검색
   *
   * @param request 검색 조건
   * @param userId                   유저 아이디
   * @param role                     USER
   * @param pageable                 pageable
   * @return 검색 예약 내역
   */
  Page<ReservationGetResponse> searchReservation(
      ReservationSearchRequest request,
      String userId,
      Role role,
      Pageable pageable);

  /**
   * 관리자 예약 검색
   *
   * @param request 검색 조건
   * @param userId                   유저 아이디
   * @param role                     ADMIN, HOST
   * @param pageable                 pageable
   * @return 검색 예약 내역
   */
  Page<ReservationGetResponse> search(
      ReservationAdminSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  );

  /**
   * 예약 수정 (결제 완료 or 취소) 결제 완료시 숙소, 쿠폰, 포인트의 상태를 변경합니다.
   *
   * @param reservationUpdateRequest paymentId, 예약 상태 COMPLETED or CANCELED
   * @param reservationId            예약 아이디
   * @param userId                   유저 아이디
   * @param role                     권한
   */
  void updateReservation(
      ReservationUpdateRequest reservationUpdateRequest,
      String reservationId,
      String userId,
      Role role
  );

  /**
   * 예약 취소
   *
   * @param reservationId 예약 아이디
   * @param request       취소 사유
   * @param userId        유저 아이디
   * @param role          USER
   */
  void cancelReservation(
      String reservationId,
      ReservationCancelRequest request,
      String userId,
      Role role
  );
}
