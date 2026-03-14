package com.aliyara.billingservice.application.dto.quote;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuoteRequest {

    // Extracted from security context
    private String tenantId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Client name is required")
    private String clientName;

    private String clientEmail;

    @NotEmpty(message = "Quote must have at least one line")
    @Valid
    private List<QuoteLineRequest> lines;
}
