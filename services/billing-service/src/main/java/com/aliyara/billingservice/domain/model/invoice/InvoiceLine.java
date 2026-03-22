package com.aliyara.billingservice.domain.model.invoice;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class InvoiceLine {

    private final UUID id;
    private final UUID productId;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal lineTotal;

    private InvoiceLine(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.productId = builder.productId;
        this.productName = builder.productName;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
        this.lineTotal = builder.unitPrice.multiply(BigDecimal.valueOf(builder.quantity));
    }

    private void validate(Builder builder) {
        if (builder.productId == null) {
            throw new IllegalArgumentException("InvoiceLine productId cannot be null");
        }
        if (builder.quantity <= 0) {
            throw new IllegalArgumentException("InvoiceLine quantity must be greater than 0");
        }
        if (builder.unitPrice == null || builder.unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("InvoiceLine unitPrice must be positive");
        }
    }

    public static class Builder {
        private UUID id;
        private UUID productId;
        private String productName;
        private int quantity;
        private BigDecimal unitPrice;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public InvoiceLine build() {
            return new InvoiceLine(this);
        }
    }
}
