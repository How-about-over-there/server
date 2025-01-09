package com.haot.lodge.application.facade;


import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.presentation.request.LodgeDateUpdateRequest;
import com.haot.lodge.presentation.response.LodgeDateReadResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LodgeDateFacade {

    private final LodgeService lodgeService;
    private final LodgeDateService lodgeDateService;

    @Transactional(readOnly = true)
    public Slice<LodgeDateReadResponse> readLodgeDates(
            Pageable pageable, String lodgeId, LocalDate start, LocalDate end
    ) {
        Lodge lodge = lodgeService.getValidLodgeById(lodgeId);
        return lodgeDateService
                .readAll(pageable, lodge, start, end)
                .map(LodgeDateReadResponse::new);
    }

    @Transactional
    public void updatePrice(String dateId, Double requestPrice) {
        LodgeDate lodgeDate = lodgeDateService.getValidLodgeDateByIdWithLock(dateId);
        // TODO: 권한 HOST 라면 lodgeHostId와 로그인한 유저 아이디 같은지 확인 필요
        lodgeDate.updatePrice(requestPrice);
    }

    @Transactional
    public void updateStatus(List<String> ids, String requestStatus) {
        ReservationStatus status = ReservationStatus.fromString(requestStatus);
        ids.forEach(id -> {
            LodgeDate lodgeDate = lodgeDateService.getValidLodgeDateById(id);
            lodgeDateService.updateStatus(lodgeDate, status);
        });
    }
}
