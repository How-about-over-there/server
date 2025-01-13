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
    public LodgeDate getValidLodgeDateByIdWithLock(String lodgeDateId) {
        return lodgeDateRepository.findByIdWithLock(lodgeDateId)
                .orElseThrow(()-> new LodgeException(ErrorCode.LODGE_DATE_NOT_FOUND));
    }

    @Override
    public void create(
            Lodge lodge, LocalDate startDate, LocalDate endDate, List<LocalDate> excludeDates
    ) {
        checkForExistingLodgeDates(lodge.getId(), startDate, endDate);
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

    @Override
    public void deleteAllByLodge(Lodge lodge, String userId) {
        lodge.getDates().forEach(lodgeDate -> {
            dateDeleteValidation(lodgeDate);
            lodgeDate.deleteEntity(userId);
        });
    }

    private void dateDeleteValidation(LodgeDate lodgeDate) {
        if(lodgeDate.getDate().isAfter(LocalDate.now())
                && lodgeDate.getStatus()==ReservationStatus.COMPLETE
        ) {
            throw new LodgeException(ErrorCode.CANNOT_DELETE_SCHEDULED_DATE);
        }
    }

    private void checkForExistingLodgeDates(
            String lodgeId, LocalDate startDate, LocalDate endDate
    ) {
        if (lodgeDateRepository.existsOverlappingDates(lodgeId, startDate, endDate)) {
            throw new LodgeException(ErrorCode.OVERLAPPING_DATES);
        }
    }

    private void dateValidation(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (ChronoUnit.DAYS.between(today, startDate) > 365) {
            throw new LodgeException(ErrorCode.START_DATE_TOO_FAR);
        }
        if (startDate.isBefore(today)) {
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
