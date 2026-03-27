package com.aliyara.billingservice.application.dto.invoice;

import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import com.aliyara.billingservice.domain.model.common.CustomerSnapshot;
import com.aliyara.billingservice.domain.model.common.DocumentStatus;
import com.aliyara.billingservice.domain.model.invoice.enums.InvoiceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceResponse {
    private UUID id;
    private String tenantId;
    private UUID clientId;
    private UUID quoteId;
    private InvoiceStatus status;
    private List<InvoiceLineResponse> lines;
    private BigDecimal subtotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private CompanySnapshot companySnapshot;
    private CustomerSnapshot customerSnapshot;
    private DocumentStatus documentStatus;
    private String documentUrl;
    private LocalDateTime generatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
