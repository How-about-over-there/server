package com.haot.lodge.presentation.request.lodgedate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "숙소 날짜의 상태를 변경하기 위한 요청")
public record LodgeDateUpdateStatusRequest(
        @NotNull List<String> lodgeDateIds,
        @NotNull String status
) {
}
