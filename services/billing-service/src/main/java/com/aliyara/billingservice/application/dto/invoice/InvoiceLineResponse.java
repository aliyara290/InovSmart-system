package com.aliyara.billingservice.application.dto.invoice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceLineResponse {
    private UUID id;
    private UUID productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
