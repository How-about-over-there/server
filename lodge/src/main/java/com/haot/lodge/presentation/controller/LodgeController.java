package com.haot.lodge.presentation.controller;


import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.facade.LodgeFacade;
import com.haot.lodge.presentation.response.LodgeReadAllResponse;
import com.haot.lodge.presentation.request.LodgeCreateRequest;
import com.haot.lodge.presentation.request.LodgeUpdateRequest;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.presentation.response.LodgeCreateResponse;
import com.haot.lodge.presentation.response.LodgeReadOneResponse;
import com.haot.submodule.role.Role;
import com.haot.submodule.role.RoleCheck;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lodges")
@RequiredArgsConstructor
public class LodgeController {

    private final LodgeFacade lodgeFacade;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @RoleCheck({Role.ADMIN, Role.HOST})
    public ApiResponse<LodgeCreateResponse> create(
            @RequestHeader("X-User-Id") String userId,
            @Valid LodgeCreateRequest request
    ) {
        LodgeResponse lodge = lodgeFacade.createLodge(userId, request);
        return ApiResponse.success(new LodgeCreateResponse(lodge));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<Slice<LodgeReadAllResponse>> readAll(
            @PageableDefault(size = 30)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdAt", direction = Direction.DESC),
                    @SortDefault(sort = "updatedAt", direction = Direction.DESC)
            })
            Pageable pageable,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "maxReservationDay", required = false) Integer maxReservationDay,
            @RequestParam(name = "maxPersonnel", required = false) Integer maxPersonnel,
            @RequestParam(name = "checkInDate", required = false) LocalDate checkInDate,
            @RequestParam(name = "checkOutDate", required = false) LocalDate checkOutDate
    ) {
        return ApiResponse.success(
                lodgeFacade.readAllLodgeBy(
                        pageable, name, address, maxReservationDay, maxPersonnel, checkInDate, checkOutDate)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{lodgeId}")
    public ApiResponse<LodgeReadOneResponse> readOne(
            @PathVariable String lodgeId
    ) {
        return ApiResponse.success(lodgeFacade.readLodge(lodgeId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{lodgeId}")
    @RoleCheck({Role.ADMIN, Role.HOST})
    public ApiResponse<Void> update(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role userRole,
            @PathVariable String lodgeId,
            @Valid @RequestBody LodgeUpdateRequest request
    ) {
        lodgeFacade.updateLodge(userRole, userId, lodgeId, request);
        return ApiResponse.success();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{lodgeId}")
    @RoleCheck({Role.ADMIN, Role.HOST})
    public ApiResponse<Void> delete(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") Role userRole,
            @PathVariable String lodgeId
    ) {
        lodgeFacade.deleteLodge(userRole, userId, lodgeId);
        return ApiResponse.success();
    }

    /**
     * 숙소 유효성 검사 API
     * @param lodgeId 검사를 진행 할 숙소 ID
     * @return 유효성
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{lodgeId}/verify")
    public Map<String, Boolean> verify(
            @PathVariable(name = "lodgeId") String lodgeId
    ) {
        return Map.of("validity", true);
    }

}
