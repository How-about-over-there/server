package com.haot.lodge.application.service;


import com.haot.lodge.application.service.Impl.LodgeDateService;
import com.haot.lodge.application.service.Impl.LodgeImageService;
import com.haot.lodge.application.service.Impl.LodgeRuleService;
import com.haot.lodge.application.service.Impl.LodgeService;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.presentation.request.LodgeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class Service {

    private final LodgeService lodgeService;
    private final LodgeImageService lodgeImageService;
    private final LodgeRuleService lodgeRuleService;
    private final LodgeDateService lodgeDateService;

    @Transactional
    public void createLodge(String userId, LodgeCreateRequest request) {
        // TODO: UserId 유효성 검증
        Lodge lodge = lodgeService.create(
                userId, request.name(), request.description(), request.address(),
                request.term(), request.basicPrice());
        lodgeImageService.create(lodge, request.image(), request.imageTitle(), request.imageDescriptions());
        lodgeRuleService.create(lodge, request.maxReservationDay(), request.maxPersonnel(), request.customization());
        lodgeDateService.create(lodge, request.startDate(), request.endDate(), request.excludeDates());
    }

}













