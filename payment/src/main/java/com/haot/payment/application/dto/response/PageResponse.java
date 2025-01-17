package com.haot.payment.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "결제 전체 조회 및 검색 응답 DTO")
public record PageResponse<T> (
        int totalPages,
        int pageNumber,
        List<T> content
) {
    public static <T> PageResponse<T> of(Page<T> page){
        return new PageResponse<>(
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getContent()
        );
    }
}