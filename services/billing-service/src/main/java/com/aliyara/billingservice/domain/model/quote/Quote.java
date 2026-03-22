package com.aliyara.billingservice.domain.model.quote;

import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import com.aliyara.billingservice.domain.model.common.CustomerSnapshot;
import com.aliyara.billingservice.domain.model.common.DocumentStatus;
import com.aliyara.billingservice.domain.model.quote.enums.QuoteStatus;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Quote {

    private final UUID id;
    private final String tenantId;
    private final UUID clientId;
    private QuoteStatus status;
    private List<QuoteLine> lines;
    private BigDecimal subtotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private CompanySnapshot companySnapshot;
    private CustomerSnapshot customerSnapshot;
    private DocumentStatus documentStatus;
    private String documentUrl;
    private LocalDateTime generatedAt;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Quote(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.tenantId = builder.tenantId;
        this.clientId = builder.clientId;
        this.status = builder.status != null ? builder.status : QuoteStatus.DRAFT;
        this.lines = builder.lines != null ? new ArrayList<>(builder.lines) : new ArrayList<>();
        this.taxRate = builder.taxRate != null ? builder.taxRate : BigDecimal.ZERO;
        this.companySnapshot = builder.companySnapshot;
        this.customerSnapshot = builder.customerSnapshot;
        this.documentStatus = builder.documentStatus;
        this.documentUrl = builder.documentUrl;
        this.generatedAt = builder.generatedAt;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
        recalculateTotals();
    }

    public void addLine(QuoteLine line) {
        ensureModifiable();
        this.lines.add(line);
        recalculateTotals();
        this.updatedAt = LocalDateTime.now();
    }

    public void replaceLines(List<QuoteLine> newLines) {
        ensureModifiable();
        this.lines = new ArrayList<>(newLines);
        recalculateTotals();
        this.updatedAt = LocalDateTime.now();
    }

    public void send() {
        if (this.status != QuoteStatus.DRAFT) {
            throw new IllegalStateException("Quote can only be sent from DRAFT status. Current: " + this.status);
        }
        if (this.lines.isEmpty()) {
            throw new IllegalStateException("Quote must have at least one line to be sent");
        }
        this.status = QuoteStatus.SENT;
        this.updatedAt = LocalDateTime.now();
    }

    public void accept() {
        if (this.status != QuoteStatus.SENT) {
            throw new IllegalStateException("Quote can only be accepted from SENT status. Current: " + this.status);
        }
        this.status = QuoteStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        if (this.status != QuoteStatus.SENT && this.status != QuoteStatus.ACCEPTED) {
            throw new IllegalStateException("Quote can only be rejected from SENT or ACCEPTED status. Current: " + this.status);
        }
        this.status = QuoteStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void startDocumentGeneration(CompanySnapshot companySnapshot, CustomerSnapshot customerSnapshot) {
        this.companySnapshot = companySnapshot;
        this.customerSnapshot = customerSnapshot;
        this.documentStatus = DocumentStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }

    public void markDocumentReady(String documentUrl) {
        this.documentUrl = documentUrl;
        this.documentStatus = DocumentStatus.READY;
        this.generatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markDocumentFailed() {
        this.documentStatus = DocumentStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    public List<QuoteLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private void ensureModifiable() {
        if (this.status != QuoteStatus.DRAFT) {
            throw new IllegalStateException("Quote can only be modified in DRAFT status. Current: " + this.status);
        }
    }

    private void recalculateTotals() {
        this.subtotal = lines.stream()
                .map(QuoteLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.taxAmount = this.subtotal.multiply(this.taxRate);
        this.total = this.subtotal.add(this.taxAmount);
    }

    private void validate(Builder builder) {
        if (builder.tenantId == null || builder.tenantId.isBlank()) {
            throw new IllegalArgumentException("Quote tenantId cannot be empty");
        }
        if (builder.clientId == null) {
            throw new IllegalArgumentException("Quote clientId cannot be null");
        }
    }

    public static class Builder {
        private UUID id;
        private String tenantId;
        private UUID clientId;
        private QuoteStatus status;
        private List<QuoteLine> lines;
        private BigDecimal taxRate;
        private CompanySnapshot companySnapshot;
        private CustomerSnapshot customerSnapshot;
        private DocumentStatus documentStatus;
        private String documentUrl;
        private LocalDateTime generatedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder clientId(UUID clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder status(QuoteStatus status) {
            this.status = status;
            return this;
        }

        public Builder lines(List<QuoteLine> lines) {
            this.lines = lines;
            return this;
        }

        public Builder taxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public Builder companySnapshot(CompanySnapshot companySnapshot) {
            this.companySnapshot = companySnapshot;
            return this;
        }

        public Builder customerSnapshot(CustomerSnapshot customerSnapshot) {
            this.customerSnapshot = customerSnapshot;
            return this;
        }

        public Builder documentStatus(DocumentStatus documentStatus) {
            this.documentStatus = documentStatus;
            return this;
        }

        public Builder documentUrl(String documentUrl) {
            this.documentUrl = documentUrl;
            return this;
        }

        public Builder generatedAt(LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Quote build() {
            return new Quote(this);
        }
    }
}
