package com.aliyara.inventoryservice.domain.stock;

import com.aliyara.inventoryservice.domain.stock.enums.StockStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Stock {

    private final UUID id;
    private int quantityTotal;
    private int quantityReserved;
    private StockStatus status;
    private int minStock;
    private int maxStock;
    private LocalDateTime lastUpdatedAt;
    private final UUID productId;
    private final List<StockHistory> stockHistories;

    private Stock(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.quantityTotal = builder.quantityTotal;
        this.quantityReserved = builder.quantityReserved;
        this.status = builder.status != null ? builder.status : StockStatus.IN_STOCK;
        this.minStock = builder.minStock;
        this.maxStock = builder.maxStock;
        this.lastUpdatedAt = builder.lastUpdatedAt != null ? builder.lastUpdatedAt : LocalDateTime.now();
        this.productId = builder.productId;
        this.stockHistories = new ArrayList<>(builder.stockHistories != null ? builder.stockHistories : List.of());
    }

    public static class Builder {
        private UUID id;
        private int quantityTotal;
        private int quantityReserved;
        private StockStatus status;
        private int minStock;
        private int maxStock;
        private LocalDateTime lastUpdatedAt;
        private UUID productId;
        private List<StockHistory> stockHistories;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder quantityTotal(int quantityTotal) {
            this.quantityTotal = quantityTotal;
            return this;
        }

        public Builder quantityReserved(int quantityReserved) {
            this.quantityReserved = quantityReserved;
            return this;
        }

        public Builder status(StockStatus status) {
            this.status = status;
            return this;
        }

        public Builder minStock(int minStock) {
            this.minStock = minStock;
            return this;
        }

        public Builder maxStock(int maxStock) {
            this.maxStock = maxStock;
            return this;
        }

        public Builder lastUpdatedAt(LocalDateTime lastUpdatedAt) {
            this.lastUpdatedAt = lastUpdatedAt;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder stockHistories(List<StockHistory> stockHistories) {
            this.stockHistories = stockHistories;
            return this;
        }

        public Stock build() {
            return new Stock(this);
        }
    }

    public List<StockHistory> getStockHistories() {
        return Collections.unmodifiableList(stockHistories);
    }

    public void addHistory(StockHistory history) {
        if (history == null) {
            throw new IllegalArgumentException("StockHistory cannot be null");
        }
        this.stockHistories.add(history);
    }

    public void adjustQuantity(int newQuantityTotal) {
        if (newQuantityTotal < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.quantityTotal = newQuantityTotal;
        this.lastUpdatedAt = LocalDateTime.now();
        refreshStatus();
    }

    public void reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Reserve quantity must be positive");
        }
        int available = quantityTotal - quantityReserved;
        if (quantity > available) {
            throw new IllegalStateException("Insufficient available stock to reserve");
        }
        this.quantityReserved += quantity;
        this.lastUpdatedAt = LocalDateTime.now();
        refreshStatus();
    }

    public void releaseReservation(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Release quantity must be positive");
        }
        if (quantity > quantityReserved) {
            throw new IllegalStateException("Cannot release more than what is reserved");
        }
        this.quantityReserved -= quantity;
        this.lastUpdatedAt = LocalDateTime.now();
        refreshStatus();
    }

    public int getAvailableQuantity() {
        return quantityTotal - quantityReserved;
    }

    public boolean isOverstock() {
        return maxStock > 0 && quantityTotal > maxStock;
    }

    private void refreshStatus() {
        if (quantityTotal == 0) {
            this.status = StockStatus.OUT_OF_STOCK;
        } else if (quantityTotal <= minStock) {
            this.status = StockStatus.LOW_STOCK;
        } else {
            this.status = StockStatus.IN_STOCK;
        }
    }

    private void validate(Builder builder) {
        if (builder.productId == null) {
            throw new IllegalArgumentException("Stock productId cannot be null");
        }
        if (builder.quantityTotal < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        if (builder.quantityReserved < 0) {
            throw new IllegalArgumentException("Stock reserved quantity cannot be negative");
        }
        if (builder.minStock < 0) {
            throw new IllegalArgumentException("Stock minStock cannot be negative");
        }
        if (builder.maxStock < 0) {
            throw new IllegalArgumentException("Stock maxStock cannot be negative");
        }
    }
}