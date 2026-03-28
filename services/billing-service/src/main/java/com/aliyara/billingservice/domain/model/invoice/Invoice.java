package com.aliyara.billingservice.domain.model.invoice;

import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import com.aliyara.billingservice.domain.model.common.CustomerSnapshot;
import com.aliyara.billingservice.domain.model.common.DocumentStatus;
import com.aliyara.billingservice.domain.model.invoice.enums.InvoiceStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Invoice {

    private final UUID id;
    private final String tenantId;
    private final UUID clientId;
    private final UUID quoteId;
    private InvoiceStatus status;
    private List<InvoiceLine> lines;
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

    private Invoice(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.tenantId = builder.tenantId;
        this.clientId = builder.clientId;
        this.quoteId = builder.quoteId;
        this.status = builder.status != null ? builder.status : InvoiceStatus.DRAFT;
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

    public void markSent() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Invoice can only be sent from DRAFT status. Current: " + this.status);
        }
        this.status = InvoiceStatus.SENT;
        this.updatedAt = LocalDateTime.now();
    }

    public void markPaid() {
        if (this.status != InvoiceStatus.SENT) {
            throw new IllegalStateException("Invoice can only be paid from SENT status. Current: " + this.status);
        }
        this.status = InvoiceStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("Invoice cannot be cancelled from " + this.status + " status");
        }
        this.status = InvoiceStatus.CANCELLED;
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

    public List<InvoiceLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private void recalculateTotals() {
        this.subtotal = lines.stream()
                .map(InvoiceLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.taxAmount = this.subtotal.multiply(this.taxRate);
        this.total = this.subtotal.add(this.taxAmount);
    }

    private void validate(Builder builder) {
        if (builder.tenantId == null || builder.tenantId.isBlank()) {
            throw new IllegalArgumentException("Invoice tenantId cannot be empty");
        }
        if (builder.clientId == null) {
            throw new IllegalArgumentException("Invoice clientId cannot be null");
        }
    }

    public static class Builder {
        private UUID id;
        private String tenantId;
        private UUID clientId;
        private UUID quoteId;
        private InvoiceStatus status;
        private List<InvoiceLine> lines;
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

        public Builder quoteId(UUID quoteId) {
            this.quoteId = quoteId;
            return this;
        }

        public Builder status(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        public Builder lines(List<InvoiceLine> lines) {
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

        public Invoice build() {
            return new Invoice(this);
        }
    }
}
