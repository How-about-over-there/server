package com.haot.payment.application.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
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
