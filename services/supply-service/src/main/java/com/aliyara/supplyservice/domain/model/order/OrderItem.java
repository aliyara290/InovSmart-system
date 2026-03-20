package com.aliyara.supplyservice.domain.model.order;


import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderItem {

    private final UUID id;
    private final UUID productId;
    private int quantity;
    private BigDecimal unitPrice;
    private int deliveredQuantity;

    private OrderItem(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.productId = builder.productId;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
        this.deliveredQuantity = builder.deliveredQuantity;
    }

    private void validate(Builder builder) {
        if (builder.productId == null) {
            throw new IllegalArgumentException("OrderItem productId cannot be null");
        }
        if (builder.quantity <= 0) {
            throw new IllegalArgumentException("OrderItem quantity must be greater than zero");
        }
        if (builder.unitPrice == null || builder.unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("OrderItem unitPrice cannot be negative or null");
        }
    }

    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static class Builder {
        private UUID id;
        private UUID productId;
        private int quantity;
        private BigDecimal unitPrice = BigDecimal.ZERO;
        private int deliveredQuantity = 0;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
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

        public Builder deliveredQuantity(int deliveredQuantity) {
            this.deliveredQuantity = deliveredQuantity;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}

