package com.haot.lodge.application.facade;


import com.haot.lodge.application.dto.LodgeSearchCriteria;
import com.haot.lodge.application.dto.LodgeImageDto;
import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeRuleDto;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeImageService;
import com.haot.lodge.application.service.LodgeRuleService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeRule;
import com.haot.lodge.presentation.request.lodge.LodgeCreateRequest;
import com.haot.lodge.presentation.request.lodge.LodgeSearchParams;
import com.haot.lodge.presentation.request.lodge.LodgeUpdateRequest;
import com.haot.lodge.application.response.LodgeReadAllResponse;
import com.haot.lodge.application.response.LodgeReadOneResponse;
import com.haot.submodule.role.Role;
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

    @Transactional(readOnly = true)
    public boolean lodgeValidation(String lodgeId) {
        return lodgeService.isValidLodgeId(lodgeId);
    }

    @Transactional
    public LodgeDto createLodge(String userId, LodgeCreateRequest request) {
        Lodge lodge = lodgeService.create(
                userId, request.name(), request.description(),
                request.address(), request.term(), request.basicPrice());
        lodgeImageService.create(lodge, request.image(), request.imageTitle(), request.imageDescription());
        lodgeRuleService.create(lodge, request.maxReservationDay(), request.maxPersonnel(), request.customization());
        lodgeDateService.create(lodge, request.startDate(), request.endDate(), request.excludeDates());
        return LodgeDto.from(lodge);
    }

    @Transactional(readOnly = true)
    public LodgeReadOneResponse readLodge(String lodgeId) {
        Lodge lodge = lodgeService.getValidLodgeById(lodgeId);
        LodgeRule rule = lodgeRuleService.getLodgeRuleByLodgeId(lodgeId);
        return new LodgeReadOneResponse(
                LodgeDto.from(lodge),
                lodge.getImages()
                        .stream()
                        .map(LodgeImageDto::from)
                        .toList(),
                LodgeRuleDto.from(rule)
        );
    }

    @Transactional(readOnly = true)
    public Slice<LodgeReadAllResponse> readAllLodgeBy(
            Pageable pageable, LodgeSearchParams params
    ) {
        return lodgeService
                .readAllBy(pageable, LodgeSearchCriteria.of(params, isOnlyCheckIn(params)))
                .map(lodge -> new LodgeReadAllResponse(
                        LodgeDto.from(lodge),
                        lodge.getImages()
                                .stream()
                                .map(LodgeImageDto::from)
                                .toList()
                ));
    }

    private boolean isOnlyCheckIn(LodgeSearchParams params) {
        return (params.checkInDate()!= null && params.checkOutDate() == null);
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