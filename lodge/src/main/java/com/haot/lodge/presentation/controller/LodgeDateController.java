package com.haot.lodge.presentation.controller;


import com.haot.lodge.application.facade.LodgeDateFacade;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.common.response.SliceResponse;
import com.haot.lodge.presentation.request.LodgeDateAddRequest;
import com.haot.lodge.presentation.request.LodgeDateSearchParams;
import com.haot.lodge.presentation.request.LodgeDateUpdateRequest;
import com.haot.lodge.presentation.request.LodgeDateUpdateStatusRequest;
import com.haot.lodge.presentation.response.LodgeDateReadResponse;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lodge-dates")
@RequiredArgsConstructor
public class LodgeDateController {

    private final LodgeDateFacade lodgeDateFacade;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @RoleCheck({Role.ADMIN, Role.HOST})
    public ApiResponse<Void> add(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role userRole,
            @Valid @RequestBody LodgeDateAddRequest dateAddRequest
    ) {
        lodgeDateFacade.addLodgeDate(userRole, userId, dateAddRequest);
        return ApiResponse.success();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<SliceResponse<LodgeDateReadResponse>> readAll(
            @PageableDefault(size = 30)
            @SortDefault(sort = "date", direction = Direction.ASC)
            Pageable pageable,
            @Valid @ModelAttribute LodgeDateSearchParams searchParams
    ) {
        return ApiResponse.success(SliceResponse.of(
                lodgeDateFacade.readLodgeDates(pageable, searchParams)));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{dateId}")
    @RoleCheck({Role.ADMIN, Role.HOST})
    public ApiResponse<Void> update(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role userRole,
            @PathVariable String dateId,
            @RequestBody LodgeDateUpdateRequest request
    ) {
        lodgeDateFacade.updatePrice(userRole, userId, dateId, request.price());
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
