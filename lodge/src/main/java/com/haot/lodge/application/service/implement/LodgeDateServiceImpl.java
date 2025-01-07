package com.haot.lodge.application.service.implement;


import com.haot.lodge.application.response.LodgeDateResponse;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LodgeDateServiceImpl implements LodgeDateService {

    private final LodgeDateRepository lodgeDateRepository;
    private final Integer MIN_RANGE = 30;
    private final Integer MAX_RANGE = 365;


    @Override
    public LodgeDate getValidLodgeDateById(String lodgeDateId) {
        return lodgeDateRepository.findById(lodgeDateId)
                .orElseThrow(()-> new LodgeException(ErrorCode.LODGE_DATE_NOT_FOUND));
    }

    @Override
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

    @Override
    public Slice<LodgeDateResponse> readAll(
            Pageable pageable, Lodge lodge, LocalDate start, LocalDate end
    ) {
        return lodgeDateRepository
                .findAllLodgeDateByRange(pageable, lodge, start, end)
                .map(LodgeDateResponse::from);
    }

    @Override
    public void updateStatus(
            LodgeDate lodgeDate, ReservationStatus status
    ) {
        lodgeDate.updateStatus(status);
    }

    /**
     * 입력된 날짜 유효성 검사
     * @param startDate 숙소 시작 날짜
     * @param endDate 숙소 끝 날짜
     */
    private void dateValidation(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new LodgeException(ErrorCode.START_DATE_IN_PAST);
        }
        if (startDate.isAfter(endDate)) {
            throw new LodgeException(ErrorCode.START_DATE_AFTER_END_DATE);
        }
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (daysBetween < MIN_RANGE) {
            throw new LodgeException(ErrorCode.DATE_RANGE_TOO_SHORT);
        }
        if (daysBetween > MAX_RANGE) {
            throw new LodgeException(ErrorCode.DATE_RANGE_TOO_LONG);
        }
    }
}
