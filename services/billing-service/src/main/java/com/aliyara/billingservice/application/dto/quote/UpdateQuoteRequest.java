package com.aliyara.billingservice.application.dto.quote;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateQuoteRequest {

    @NotEmpty(message = "lines must not be empty")
    @Valid
    private List<QuoteLineRequest> lines;

    private BigDecimal taxRate;
}
