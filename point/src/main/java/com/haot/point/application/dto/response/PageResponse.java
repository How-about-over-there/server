package com.haot.point.application.dto.response;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

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
