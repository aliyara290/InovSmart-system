package com.aliyara.billingservice.domain.model.invoice;

import com.aliyara.billingservice.domain.model.invoice.enums.InvoiceStatus;
import com.aliyara.billingservice.domain.exception.InvalidStatusTransitionException;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Invoice {

    private final UUID id;
    private final String title;
    private final String tenantId;
    private final UUID quoteId;
    private final String invoiceNumber;
    private InvoiceStatus status;
    private BigDecimal totalHT;
    private BigDecimal totalTTC;
    private final LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private final List<InvoiceLine> lines;

    private Invoice(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.title = builder.title;
        this.tenantId = builder.tenantId;
        this.quoteId = builder.quoteId;
        this.invoiceNumber = builder.invoiceNumber;
        this.status = builder.status != null ? builder.status : InvoiceStatus.DRAFT;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.dueDate = builder.dueDate;
        this.lines = builder.lines != null ? new ArrayList<>(builder.lines) : new ArrayList<>();
        calculateTotals();
        validate();
    }

    public List<InvoiceLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    // ── Behavior ──────────────────────────────────────────────────────────────

    public void markAsSent() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new InvalidStatusTransitionException("Cannot transition from " + this.status + " to SENT.");
        }
        this.status = InvoiceStatus.SENT;
    }

    public void markAsPaid() {
        if (this.status != InvoiceStatus.SENT && this.status != InvoiceStatus.OVERDUE) {
            throw new InvalidStatusTransitionException("Cannot transition from " + this.status + " to PAID.");
        }
        this.status = InvoiceStatus.PAID;
    }

    public void markAsOverdue() {
        if (this.status != InvoiceStatus.SENT) {
            throw new InvalidStatusTransitionException("Cannot transition from " + this.status + " to OVERDUE.");
        }
        this.status = InvoiceStatus.OVERDUE;
    }

    // ── Internal Helpers ──────────────────────────────────────────────────────

    private void calculateTotals() {
        BigDecimal ht = BigDecimal.ZERO;
        BigDecimal ttc = BigDecimal.ZERO;

        for (InvoiceLine line : lines) {
            ht = ht.add(line.getLineTotal());
            BigDecimal taxAmount = line.getLineTotal()
                    .multiply(line.getTaxRate())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            ttc = ttc.add(line.getLineTotal().add(taxAmount));
        }

        this.totalHT = ht.setScale(4, RoundingMode.HALF_UP);
        this.totalTTC = ttc.setScale(4, RoundingMode.HALF_UP);
    }

    private void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Tenant ID cannot be empty");
        }
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be empty");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Invoice must contain at least one line");
        }
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        private UUID id;
        private String title;
        private String tenantId;
        private UUID quoteId;
        private String invoiceNumber;
        private InvoiceStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime dueDate;
        private List<InvoiceLine> lines = new ArrayList<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder quoteId(UUID quoteId) {
            this.quoteId = quoteId;
            return this;
        }

        public Builder invoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public Builder status(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder lines(List<InvoiceLine> lines) {
            this.lines = lines;
            return this;
        }

        public Invoice build() {
            return new Invoice(this);
        }
    }
}
