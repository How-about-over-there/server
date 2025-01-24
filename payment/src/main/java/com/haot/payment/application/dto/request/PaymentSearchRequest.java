package com.haot.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "결제 검색 REQUEST DTO")
public class PaymentSearchRequest {
    private String userId;
    private String reservationId;
    private String merchantId;
    @Schema(description = "CARD, TRANSFER 만 요청 가능합니다.")
    private String method;
    private String status;
    private Double minPrice;
    private Double maxPrice;
    private LocalDate start;
    private LocalDate end;
}
