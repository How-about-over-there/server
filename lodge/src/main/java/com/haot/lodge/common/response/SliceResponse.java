package com.haot.lodge.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Slice;

@Schema(description = "Slice 형식 응답")
public record SliceResponse<T>(
        boolean hasNext,
        int sliceNumber,
        int numberOfElements,
        List<T> content
) {
    public static <T> SliceResponse<T> of(Slice<T> slice){
        return new SliceResponse<>(
                slice.hasNext(),
                slice.getNumber(),
                slice.getNumberOfElements(),
                slice.getContent()
        );
    }
}
