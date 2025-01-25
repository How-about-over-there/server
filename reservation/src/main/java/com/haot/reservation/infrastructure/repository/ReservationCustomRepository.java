package com.haot.reservation.infrastructure.repository;

import com.haot.reservation.application.dto.req.ReservationAdminSearchRequest;
import com.haot.reservation.application.dto.req.ReservationSearchRequest;
import com.haot.reservation.domain.model.Reservation;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationCustomRepository {

  Page<Reservation> searchReservation(
      ReservationSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  );

  Page<Reservation> search(
      ReservationAdminSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  );
}
