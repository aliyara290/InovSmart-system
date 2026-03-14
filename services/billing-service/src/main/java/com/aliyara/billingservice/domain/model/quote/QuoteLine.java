package com.aliyara.billingservice.domain.model.quote;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
public class QuoteLine {

    private final UUID id;
    private final String productName;
    private final String description;
    private final Integer quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal taxRate;
    private final BigDecimal lineTotal;

    private QuoteLine(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.productName = builder.productName;
        this.description = builder.description;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
        this.taxRate = builder.taxRate != null ? builder.taxRate : BigDecimal.ZERO;
        this.lineTotal = calculateLineTotal();
        validate();
    }

    private BigDecimal calculateLineTotal() {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(4, RoundingMode.HALF_UP);
    }

    private void validate() {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        if (taxRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tax rate cannot be negative");
        }
    }

    public static class Builder {
        private UUID id;
        private String productName;
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal taxRate;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder taxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public QuoteLine build() {
            return new QuoteLine(this);
        }
    }
}
