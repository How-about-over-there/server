package com.haot.lodge.application.facade;


import com.haot.lodge.application.dto.LodgeDateSearchCriteria;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.common.utils.RedisLockManager;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateAddRequest;
import com.haot.lodge.presentation.request.lodgedate.LodgeDateSearchParams;
import com.haot.lodge.application.response.LodgeDateReadResponse;
import com.haot.submodule.role.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
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
    private final RedisLockManager redisLockManager;
    private static final String LOCK_PREFIX = "lock:lodgeDate:";

    @Transactional
    public void addLodgeDate(
            Role userRole, String userId, LodgeDateAddRequest request
    ) {
        Lodge lodge = lodgeService.getValidLodgeById(request.lodgeId());
        lodge.verifyProperty(userRole, userId);
        lodgeDateService.create(
                lodge, request.startDate(), request.endDate(), request.excludeDates()
        );
    }

    @Transactional(readOnly = true)
    public Slice<LodgeDateReadResponse> readLodgeDates(
            Pageable pageable, LodgeDateSearchParams params
    ) {
        Lodge lodge = lodgeService.getValidLodgeById(params.lodgeId());
        return lodgeDateService
                .readAllBy(pageable, LodgeDateSearchCriteria.of(lodge, params))
                .map(LodgeDateReadResponse::new);
    }

    @Transactional
    public void updatePrice(
            Role role, String userId, String dateId, Double requestPrice
    ) {
        LodgeDate lodgeDate = lodgeDateService.getValidLodgeDateByIdWithLock(dateId);
        lodgeDate.verifyProperty(role, userId);
        lodgeDate.updatePrice(requestPrice);
    }

    @Transactional
    public void updateStatus(List<String> dateIds, String requestStatus) {
        List<RLock> locks = redisLockManager.acquireLocks(dateIds, LOCK_PREFIX, 5, 10);
        ReservationStatus status = ReservationStatus.fromString(requestStatus);
        try {
            lodgeDateService.updateStatusOf(dateIds, status);
        } finally {
            redisLockManager.releaseLocks(locks);
        }
    }

}
