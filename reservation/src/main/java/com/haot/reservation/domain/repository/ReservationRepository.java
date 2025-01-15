package com.haot.reservation.domain.repository;

import com.haot.reservation.domain.model.Reservation;
import com.haot.reservation.infrastructure.repository.ReservationCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String>,
    ReservationCustomRepository {


}
