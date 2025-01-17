package com.haot.lodge.application.service;

import com.haot.lodge.application.dto.LodgeDateSearchCriteria;
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

    Slice<LodgeDateResponse> readAllBy(
            Pageable pageable, LodgeDateSearchCriteria searchCriteria
    );

    void updateStatusOf(List<String> lodgeDateIds, ReservationStatus newStatus);

    void deleteAllByLodge(Lodge lodge, String userId);

}
