package com.aliyara.billingservice.application.dto.quote;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class QuoteLineRequest {

    @NotNull(message = "productId is required")
    private UUID productId;

    @Positive(message = "quantity must be greater than 0")
    private int quantity;

    @NotNull(message = "unitPrice is required")
    @Positive(message = "unitPrice must be positive")
    private BigDecimal unitPrice;
}
