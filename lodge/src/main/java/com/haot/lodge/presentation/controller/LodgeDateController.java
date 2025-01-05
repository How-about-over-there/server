package com.haot.lodge.presentation.controller;


import com.haot.lodge.application.response.LodgeDateResponse;
import com.haot.lodge.application.service.Impl.LodgeDateService;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.domain.model.enums.ReservationStatus;
import com.haot.lodge.presentation.request.LodgeDateUpdateRequest;
import com.haot.lodge.presentation.request.LodgeDateUpdateStatusRequest;
import com.haot.lodge.presentation.response.LodgeDateReadResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lodge-dates")
@RequiredArgsConstructor
public class LodgeDateController {

    private final LodgeDateService lodgeDateService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<Slice<LodgeDateReadResponse>> read(
            @PageableDefault(size = 30)
            Pageable pageable,
            @RequestParam(name = "lodgeId", required = true) String lodgeId,
            @RequestParam(name = "start", required = false) LocalDate start,
            @RequestParam(name = "end", required = false) LocalDate end
    ) {
        LodgeDateResponse dto = LodgeDateResponse.builder()
                .id("UUID")
                .date(start)
                .price(120000.0)
                .status(ReservationStatus.EMPTY)
                .build();
        LodgeDateResponse dto2 = LodgeDateResponse.builder()
                .id("UUID2")
                .date(end)
                .price(120000.0)
                .status(ReservationStatus.EMPTY)
                .build();
        List<LodgeDateReadResponse> dates =
                List.of(new LodgeDateReadResponse(dto), new LodgeDateReadResponse(dto2));
        Slice<LodgeDateReadResponse> response = new SliceImpl<>(dates, pageable, false);
        return ApiResponse.success(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{dateId}")
    public ApiResponse<Void> update(
            @PathVariable String dateId,
            @RequestBody LodgeDateUpdateRequest request
    ) {
        return ApiResponse.success();
    }

    @PostMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> updateStatus(
            @Valid @RequestBody LodgeDateUpdateStatusRequest request
    ) {
        lodgeDateService.updateStatus(request.lodgeDateIds(), request.status());
        return ApiResponse.success();
    }

}
