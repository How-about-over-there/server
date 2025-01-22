package com.haot.coupon.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(description = "Page Custom RESPONSE")
@Builder
public record PageResponse<T extends Serializable>(
        int totalPages,
        int pageNumber,
        List<T> content
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 673832261630062356L;
}
