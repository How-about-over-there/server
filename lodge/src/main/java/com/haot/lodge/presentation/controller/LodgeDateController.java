package com.haot.lodge.presentation.controller;


import com.haot.lodge.application.facade.LodgeDateFacade;
import com.haot.lodge.application.service.implement.LodgeDateServiceImpl;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.presentation.request.LodgeDateUpdateRequest;
import com.haot.lodge.presentation.request.LodgeDateUpdateStatusRequest;
import com.haot.lodge.presentation.response.LodgeDateReadResponse;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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

    private final LodgeDateFacade lodgeDateFacade;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<Slice<LodgeDateReadResponse>> read(
            @PageableDefault(size = 30)
            @SortDefault(sort = "date", direction = Direction.ASC)
            Pageable pageable,
            @RequestParam(name = "lodgeId", required = true) String lodgeId,
            @RequestParam(name = "start", required = false) LocalDate start,
            @RequestParam(name = "end", required = false) LocalDate end
    ) {
        return ApiResponse.success(
                lodgeDateFacade.readLodgeDates(pageable, lodgeId, start, end)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{dateId}")
    public ApiResponse<Void> update(
            @PathVariable String dateId,
            @RequestBody LodgeDateUpdateRequest request
    ) {
        lodgeDateFacade.updatePrice(dateId, request.price());
        return ApiResponse.success();
    }

    @PostMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> updateStatus(
            @Valid @RequestBody LodgeDateUpdateStatusRequest request
    ) {
        lodgeDateFacade.updateStatus(request.lodgeDateIds(), request.status());
        return ApiResponse.success();
    }

}
