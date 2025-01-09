package com.haot.lodge.application.facade;


import com.haot.lodge.application.response.LodgeImageResponse;
import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.LodgeRuleResponse;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeImageService;
import com.haot.lodge.application.service.LodgeRuleService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.application.service.implement.LodgeServiceImpl;
import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.domain.model.LodgeRule;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.presentation.request.LodgeCreateRequest;
import com.haot.lodge.presentation.request.LodgeUpdateRequest;
import com.haot.lodge.presentation.response.LodgeReadAllResponse;
import com.haot.lodge.presentation.response.LodgeReadOneResponse;
import com.haot.submodule.role.Role;
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
public class LodgeFacade {

    private final LodgeService lodgeService;
    private final LodgeImageService lodgeImageService;
    private final LodgeRuleService lodgeRuleService;
    private final LodgeDateService lodgeDateService;

    @Transactional
    public LodgeResponse createLodge(String userId, LodgeCreateRequest request) {
        // TODO: UserId 유효성 검증
        Lodge lodge = lodgeService.create(
                userId, request.name(), request.description(),
                request.address(), request.term(), request.basicPrice());
        lodgeImageService.create(lodge, request.image(), request.imageTitle(), request.imageDescription());
        lodgeRuleService.create(lodge, request.maxReservationDay(), request.maxPersonnel(), request.customization());
        lodgeDateService.create(lodge, request.startDate(), request.endDate(), request.excludeDates());
        return LodgeResponse.from(lodge);
    }

    @Transactional(readOnly = true)
    public LodgeReadOneResponse readLodge(String lodgeId) {
        Lodge lodge = lodgeService.getValidLodgeById(lodgeId);
        LodgeRule rule = lodgeRuleService.getLodgeRuleByLodgeId(lodgeId);
        return new LodgeReadOneResponse(
                LodgeResponse.from(lodge),
                lodge.getImages()
                        .stream()
                        .map(LodgeImageResponse::from)
                        .toList(),
                LodgeRuleResponse.from(rule)
        );
    }

    @Transactional(readOnly = true)
    public Slice<LodgeReadAllResponse> readAllLodgeBy(
            Pageable pageable, String name, String address,
            Integer maxReservationDay, Integer maxPersonnel,
            LocalDate checkInDate, LocalDate checkOutDate
    ) {
        return lodgeService
                .readAllBy(pageable, name, address, maxReservationDay, maxPersonnel, checkInDate, checkOutDate)
                .map(lodge -> new LodgeReadAllResponse(
                        LodgeResponse.from(lodge),
                        lodge.getImages()
                                .stream()
                                .map(LodgeImageResponse::from)
                                .toList()
                ));
    }

    @Transactional
    public void updateLodge(
            Role userRole, String userId, String lodgeId, LodgeUpdateRequest request
    ) {
        Lodge lodge = lodgeService.getValidLodgeById(lodgeId);
        lodge.verifyProperty(userRole, userId);
        lodgeService.update(
                lodge,
                request.name(),
                request.description(),
                request.address(),
                request.term(),
                request.basicPrice()
        );
    }

    @Transactional
    public void deleteLodge(
            Role userRole, String userId, String lodgeId
    ) {
        Lodge lodge = lodgeService.getValidLodgeById(lodgeId);
        lodge.verifyProperty(userRole, userId);
        lodgeDateService.deleteAllByLodge(lodge, userId);
        lodgeImageService.deleteAllByLodge(lodge, userId);
        lodgeRuleService.deleteByLodge(lodge, userId);
        lodgeService.delete(userId, lodge);
    }

}