package com.haot.payment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "결제 검색 조건 파라미터 DTO")
public class PaymentSearchRequest {
    private String userId;
    private String reservationId;
    private String merchantId;
    private String method;
    private String status;
    private Double minPrice;
    private Double maxPrice;
    private LocalDate start;
    private LocalDate end;
}
