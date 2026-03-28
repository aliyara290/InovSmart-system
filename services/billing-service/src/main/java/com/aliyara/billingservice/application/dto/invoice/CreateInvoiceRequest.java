package com.aliyara.billingservice.application.dto.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateInvoiceRequest {

    @NotNull(message = "clientId is required")
    private UUID clientId;

    @NotEmpty(message = "lines must not be empty")
    @Valid
    private List<InvoiceLineRequest> lines;

    private BigDecimal taxRate;
}
