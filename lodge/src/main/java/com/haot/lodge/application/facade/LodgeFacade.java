package com.haot.lodge.application.facade;


import com.haot.lodge.application.response.LodgeImageResponse;
import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.LodgeRuleResponse;
import com.haot.lodge.application.service.LodgeDateService;
import com.haot.lodge.application.service.LodgeImageService;
import com.haot.lodge.application.service.LodgeRuleService;
import com.haot.lodge.application.service.LodgeService;
import com.haot.lodge.application.service.implement.LodgeServiceImpl;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeRule;
import com.haot.lodge.presentation.request.LodgeCreateRequest;
import com.haot.lodge.presentation.response.LodgeReadOneResponse;
import lombok.RequiredArgsConstructor;
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


}