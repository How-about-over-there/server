package com.haot.lodge.presentation.controller;


import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.lodgeImageResponse;
import com.haot.lodge.presentation.response.LodgeReadAllResponse;
import com.haot.lodge.application.response.LodgeRuleResponse;
import com.haot.lodge.presentation.request.LodgeCreateRequest;
import com.haot.lodge.presentation.request.LodgeDateRequest;
import com.haot.lodge.presentation.request.LodgeUpdateRequest;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.presentation.response.LodgeCreateResponse;
import com.haot.lodge.presentation.response.LodgeReadOneResponse;
import com.haot.lodge.presentation.response.LodgeReservationResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lodges")
@RequiredArgsConstructor
public class LodgeController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<LodgeCreateResponse> create(
            @Valid LodgeCreateRequest request
    ) {
        LodgeResponse info = LodgeResponse.builder()
                .id("UUID")
                .name("이름")
                .description("휴양지입니다.")
                .term(2)
                .address("경기도 고양시 고양로 551")
                .build();
        return ApiResponse.success(new LodgeCreateResponse(info));
    }
}
