package com.aliyara.billingservice.application.dto.quote;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class QuoteLineResponse {
    private UUID id;
    private String productName;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
    private BigDecimal lineTotal;
}
