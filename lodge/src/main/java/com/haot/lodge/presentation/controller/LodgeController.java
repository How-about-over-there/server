package com.haot.lodge.presentation.controller;


import com.haot.lodge.application.response.LodgeResponse;
import com.haot.lodge.application.response.lodgeImageResponse;
import com.haot.lodge.presentation.response.LodgeReadAllResponse;
import com.haot.lodge.application.response.LodgeRuleResponse;
import com.haot.lodge.presentation.request.LodgeCreateRequest;
import com.haot.lodge.presentation.request.LodgeUpdateRequest;
import com.haot.lodge.common.response.ApiResponse;
import com.haot.lodge.presentation.response.LodgeCreateResponse;
import com.haot.lodge.presentation.response.LodgeReadOneResponse;
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ApiResponse<Page<LodgeReadAllResponse>> readAll(
            @PageableDefault(size = 30)
            Pageable pageable
    ) {
        LodgeResponse info = LodgeResponse.builder()
                .id("UUID")
                .name("이름")
                .description("휴양지입니다.")
                .term(2)
                .address("경기도 고양시 고양로 551")
                .build();
        LodgeResponse info2 = LodgeResponse.builder()
                .id("UUID2")
                .name("이름")
                .description("휴양지입니다.")
                .term(2)
                .address("경기도 고양시 고양로 551")
                .build();
        lodgeImageResponse image = lodgeImageResponse.builder()
                .id("imageId")
                .url("http://s3url")
                .build();
        LodgeReadAllResponse data = LodgeReadAllResponse.builder()
                .lodge(info)
                .images(List.of(image))
                .build();
        LodgeReadAllResponse data2 = LodgeReadAllResponse.builder()
                .lodge(info2)
                .images(List.of(image))
                .build();
        List<LodgeReadAllResponse> list = List.of(data, data2);
        Page<LodgeReadAllResponse> response = new PageImpl<>(list, pageable, list.size());
        return ApiResponse.success(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{lodgeId}")
    public ApiResponse<LodgeReadOneResponse> readOne(
            @PathVariable String lodgeId
    ) {
        LodgeResponse info = LodgeResponse.builder()
                .id(lodgeId)
                .name("이름")
                .description("휴양지입니다.")
                .term(2)
                .address("경기도 고양시 고양로 551")
                .build();
        lodgeImageResponse image = lodgeImageResponse.builder()
                .id("imageId")
                .url("http://s3url")
                .build();
        LodgeRuleResponse rule = LodgeRuleResponse.builder()
                .id("ruleId")
                .maxPersonnel(12)
                .maxReservationDay(5)
                .customization("뛰지 말아주세요")
                .build();
        LodgeReadOneResponse response = LodgeReadOneResponse.builder()
                .lodge(info)
                .images(List.of(image))
                .rule(rule)
                .build();
        return ApiResponse.success(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{lodgeId}")
    public ApiResponse<Void>update(
            @PathVariable String lodgeId,
            @Valid LodgeUpdateRequest request
    ) {
        return ApiResponse.success();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{lodgeId}")
    public ApiResponse<Void> delete(
            @PathVariable String lodgeId
    ) {
        return ApiResponse.success();
    }

    @ResponseStatus(HttpStatus.OK)
    ) {
    }


}
