package com.aliyara.billingservice.application.dto.quote;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class QuoteLineResponse {
    private UUID id;
    private UUID productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
