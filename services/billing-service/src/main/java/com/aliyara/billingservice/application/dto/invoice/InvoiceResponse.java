package com.aliyara.billingservice.application.dto.invoice;

import com.aliyara.billingservice.domain.model.invoice.enums.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceResponse {
    private UUID id;
    private String title;
    private String tenantId;
    private UUID quoteId;
    private String invoiceNumber;
    private InvoiceStatus status;
    private BigDecimal totalHT;
    private BigDecimal totalTTC;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private List<InvoiceLineResponse> lines;
}
