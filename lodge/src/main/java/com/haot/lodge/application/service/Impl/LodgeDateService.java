package com.haot.lodge.application.service;


import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.domain.repository.LodgeDateRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LodgeDateService {

    private final LodgeDateRepository lodgeDateRepository;
    private final Integer MIN_RANGE = 30;
    private final Integer MAX_RANGE = 365;


    @Transactional
    public void create(
            Lodge lodge, LocalDate startDate, LocalDate endDate, List<LocalDate> excludeDates
    ) {
        dateValidation(startDate, endDate);
        Set<LocalDate> excludeDateSet = excludeDates != null
                ? new HashSet<>(excludeDates)
                : Collections.emptySet();
        List<LodgeDate> lodgeDates = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            ReservationStatus status = excludeDateSet.contains(currentDate)
                    ? ReservationStatus.UNSELECTABLE
                    : ReservationStatus.WAITING;
            LodgeDate lodgeDate = LodgeDate.create(lodge, currentDate, lodge.getBasicPrice(), status);
            lodgeDates.add(lodgeDate);
            currentDate = currentDate.plusDays(1);
        }

        lodgeDateRepository.saveAll(lodgeDates);
    }

    /**
     * 입력된 날짜 유효성 검사
     * @param startDate 숙소 시작 날짜
     * @param endDate 숙소 끝 날짜
     */
    private void dateValidation(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (daysBetween < MIN_RANGE) {
            throw new IllegalArgumentException("The date range must be at least 30 days.");
        }
        if (daysBetween > MAX_RANGE) {
            throw new IllegalArgumentException("The date range cannot exceed 365 days.");
        }
    }
}
