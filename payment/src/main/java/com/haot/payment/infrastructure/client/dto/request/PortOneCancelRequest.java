package com.haot.payment.infrastructure.client.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PortOneCancelRequest {
    String reason;
}
