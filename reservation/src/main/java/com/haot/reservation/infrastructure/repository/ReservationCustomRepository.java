package com.haot.reservation.infrastructure.repository;

import com.haot.reservation.application.dtos.req.ReservationSearchRequest;
import com.haot.reservation.domain.model.Reservation;
import com.haot.submodule.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationCustomRepository {

  Page<Reservation> search(
      ReservationSearchRequest request,
      String userId,
      Role role,
      Pageable pageable
  );

}
