package com.haot.lodge.application.service;


import com.haot.lodge.application.response.LodgeDateResponse;
import com.haot.lodge.application.response.LodgeImageResponse;
import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.LodgeRuleResponse;
import com.haot.lodge.application.service.Impl.LodgeDateService;
import com.haot.lodge.application.service.Impl.LodgeImageService;
import com.haot.lodge.application.service.Impl.LodgeRuleService;
import com.haot.lodge.application.service.Impl.LodgeBasicService;
import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeRule;
import com.haot.lodge.presentation.request.LodgeCreateRequest;
import com.haot.lodge.presentation.response.LodgeDateReadResponse;
import com.haot.lodge.presentation.response.LodgeReadOneResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LodgeService {

    private final LodgeBasicService lodgeBasicService;
    private final LodgeImageService lodgeImageService;
    private final LodgeRuleService lodgeRuleService;
    private final LodgeDateService lodgeDateService;

    @Transactional
    public LodgeResponse createLodge(String userId, LodgeCreateRequest request) {
        // TODO: UserId 유효성 검증
        Lodge lodge = lodgeBasicService.create(
                userId, request.name(), request.description(), request.address(),
                request.term(), request.basicPrice());
        lodgeImageService.create(lodge, request.image(), request.imageTitle(), request.imageDescription());
        lodgeRuleService.create(lodge, request.maxReservationDay(), request.maxPersonnel(), request.customization());
        lodgeDateService.create(lodge, request.startDate(), request.endDate(), request.excludeDates());
        return LodgeResponse.from(lodge);
    }

    @Transactional(readOnly = true)
    public LodgeReadOneResponse readLodge(String lodgeId) {
        Lodge lodge = lodgeBasicService.getLodgeById(lodgeId);
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
    public Slice<LodgeDateReadResponse> readLodgeDates(
            Pageable pageable, String lodgeId, LocalDate start, LocalDate end
    ) {
        Lodge lodge = lodgeBasicService.getLodgeById(lodgeId);
        return lodgeDateService
                .readAll(pageable, lodge, start, end)
                .map(LodgeDateReadResponse::new);
    }

}