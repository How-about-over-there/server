package com.haot.point.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Page Custom RESPONSE")
public record PageResponse<T extends Serializable>(
        int totalPages,
        int pageNumber,
        List<T> content
) implements Serializable {
    private static final long serialVersionUID = 1L;

    public static <T extends Serializable> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getContent()
        );
    }
}
