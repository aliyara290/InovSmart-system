package com.aliyara.billingservice.application.dto.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateInvoiceRequest {

    // Extracted from security context
    private String tenantId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @NotEmpty(message = "Invoice must have at least one line")
    @Valid
    private List<InvoiceLineRequest> lines;
}
