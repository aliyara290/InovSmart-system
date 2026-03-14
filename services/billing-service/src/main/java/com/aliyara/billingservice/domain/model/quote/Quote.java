package com.aliyara.billingservice.domain.model.quote;

import com.aliyara.billingservice.domain.model.quote.enums.QuoteStatus;
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
public class Quote {

    private final UUID id;
    private final String title;
    private final String tenantId;
    private final String clientName;
    private final String clientEmail;
    private QuoteStatus status;
    private BigDecimal totalHT;
    private BigDecimal totalTTC;
    private final LocalDateTime createdAt;
    private LocalDateTime validityDate;
    private final List<QuoteLine> lines;

    private Quote(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.title = builder.title;
        this.tenantId = builder.tenantId;
        this.clientName = builder.clientName;
        this.clientEmail = builder.clientEmail;
        this.status = builder.status != null ? builder.status : QuoteStatus.DRAFT;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.validityDate = builder.validityDate;
        this.lines = builder.lines != null ? new ArrayList<>(builder.lines) : new ArrayList<>();
        calculateTotals();
        validate();
    }

    public List<QuoteLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    // ── Behavior ──────────────────────────────────────────────────────────────

    public void markAsSent() {
        if (this.status != QuoteStatus.DRAFT) {
            throw new InvalidStatusTransitionException("Cannot transition from " + this.status + " to SENT.");
        }
        this.status = QuoteStatus.SENT;
    }

    public void markAsAccepted() {
        if (this.status != QuoteStatus.SENT) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from " + this.status + " to ACCEPTED. Quote must be SENT first.");
        }
        this.status = QuoteStatus.ACCEPTED;
    }

    public void markAsRejected() {
        if (this.status != QuoteStatus.SENT) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from " + this.status + " to REJECTED. Quote must be SENT first.");
        }
        this.status = QuoteStatus.REJECTED;
    }

    public void markAsExpired() {
        if (this.status == QuoteStatus.ACCEPTED || this.status == QuoteStatus.REJECTED) {
            throw new InvalidStatusTransitionException("Cannot transition from " + this.status + " to EXPIRED.");
        }
        this.status = QuoteStatus.EXPIRED;
    }

    // ── Internal Helpers ──────────────────────────────────────────────────────

    private void calculateTotals() {
        BigDecimal ht = BigDecimal.ZERO;
        BigDecimal ttc = BigDecimal.ZERO;

        for (QuoteLine line : lines) {
            ht = ht.add(line.getLineTotal());
            // TTC = HT + (HT * taxRate / 100)
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
        if (clientName == null || clientName.isBlank()) {
            throw new IllegalArgumentException("Client name cannot be empty");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Quote must contain at least one line");
        }
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        private UUID id;
        private String title;
        private String tenantId;
        private String clientName;
        private String clientEmail;
        private QuoteStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime validityDate;
        private List<QuoteLine> lines = new ArrayList<>();

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

        public Builder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder clientEmail(String clientEmail) {
            this.clientEmail = clientEmail;
            return this;
        }

        public Builder status(QuoteStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder validityDate(LocalDateTime validityDate) {
            this.validityDate = validityDate;
            return this;
        }

        public Builder lines(List<QuoteLine> lines) {
            this.lines = lines;
            return this;
        }

        public Quote build() {
            return new Quote(this);
        }
    }
}
