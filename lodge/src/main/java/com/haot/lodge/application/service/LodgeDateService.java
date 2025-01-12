package com.haot.lodge.application.service;

import com.haot.lodge.application.response.LodgeDateResponse;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LodgeDateService {

    LodgeDate getValidLodgeDateById(String lodgeDateId);

    LodgeDate getValidLodgeDateByIdWithLock(String lodgeDateId);

    void create(
            Lodge lodge,
            LocalDate startDate,
            LocalDate endDate,
            List<LocalDate> excludeDates
    );

    Slice<LodgeDateResponse> readAll(
            Pageable pageable, Lodge lodge, LocalDate start, LocalDate end
    );

    void updateStatus(LodgeDate lodgeDate, ReservationStatus requestStatus);

    void deleteAllByLodge(Lodge lodge, String userId);

}
