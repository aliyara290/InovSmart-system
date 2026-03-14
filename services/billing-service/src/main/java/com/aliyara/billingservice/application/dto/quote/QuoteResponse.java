package com.aliyara.billingservice.application.dto.quote;

import com.aliyara.billingservice.domain.model.quote.enums.QuoteStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class QuoteResponse {
    private UUID id;
    private String title;
    private String tenantId;
    private String clientName;
    private String clientEmail;
    private QuoteStatus status;
    private BigDecimal totalHT;
    private BigDecimal totalTTC;
    private LocalDateTime createdAt;
    private LocalDateTime validityDate;
    private List<QuoteLineResponse> lines;
}
