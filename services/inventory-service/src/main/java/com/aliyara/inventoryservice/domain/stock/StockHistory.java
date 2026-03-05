package com.aliyara.inventoryservice.domain.stock;

import com.aliyara.inventoryservice.domain.stock.enums.MovementType;
import com.aliyara.inventoryservice.domain.stock.enums.ReferenceType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class StockHistory {

    private final UUID id;
    private final String tenantId;
    private final UUID productId;
    private final MovementType movementType;
    private final int quantityChange;
    private final int quantityBefore;
    private final int quantityAfter;
    private final String reason;
    private final ReferenceType referenceType;
    private final UUID referenceId;
    private final UUID performedBy;

    private StockHistory(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.tenantId = builder.tenantId;
        this.productId = builder.productId;
        this.movementType = builder.movementType;
        this.quantityChange = builder.quantityChange;
        this.quantityBefore = builder.quantityBefore;
        this.quantityAfter = builder.quantityAfter;
        this.reason = builder.reason;
        this.referenceType = builder.referenceType;
        this.referenceId = builder.referenceId;
        this.performedBy = builder.performedBy;
    }

    public static class Builder {
        private UUID id;
        private String tenantId;
        private UUID productId;
        private MovementType movementType;
        private int quantityChange;
        private int quantityBefore;
        private int quantityAfter;
        private String reason;
        private ReferenceType referenceType;
        private UUID referenceId;
        private UUID performedBy;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder movementType(MovementType movementType) {
            this.movementType = movementType;
            return this;
        }

        public Builder quantityChange(int quantityChange) {
            this.quantityChange = quantityChange;
            return this;
        }

        public Builder quantityBefore(int quantityBefore) {
            this.quantityBefore = quantityBefore;
            return this;
        }

        public Builder quantityAfter(int quantityAfter) {
            this.quantityAfter = quantityAfter;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder referenceType(ReferenceType referenceType) {
            this.referenceType = referenceType;
            return this;
        }

        public Builder referenceId(UUID referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder performedBy(UUID performedBy) {
            this.performedBy = performedBy;
            return this;
        }

        public StockHistory build() {
            return new StockHistory(this);
        }
    }

    private void validate(Builder builder) {
        if (builder.tenantId == null || builder.tenantId.isBlank()) {
            throw new IllegalArgumentException("StockHistory tenantId cannot be empty");
        }
        if (builder.productId == null) {
            throw new IllegalArgumentException("StockHistory productId cannot be null");
        }
        if (builder.movementType == null) {
            throw new IllegalArgumentException("StockHistory movementType cannot be null");
        }
        if (builder.performedBy == null) {
            throw new IllegalArgumentException("StockHistory performedBy cannot be null");
        }
    }
}
