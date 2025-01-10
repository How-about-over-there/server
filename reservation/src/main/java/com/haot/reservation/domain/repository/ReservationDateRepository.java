package com.haot.reservation.domain.repository;

import com.haot.reservation.domain.model.ReservationDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, String> {

}
