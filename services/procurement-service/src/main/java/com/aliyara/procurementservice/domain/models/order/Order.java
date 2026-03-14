package com.aliyara.procurementservice.domain.models.order;

import com.aliyara.procurementservice.domain.models.order.enums.OrderStatus;
import com.aliyara.procurementservice.domain.exception.InvalidOrderStatusTransitionException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {

    private final UUID id;
    private final String tenantId;
    private final UUID supplierId;
    private OrderStatus status;
    private String notes;
    private final List<OrderItem> items;
    private BigDecimal totalExcludingTax;
    private BigDecimal totalTTC;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Order(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        validate(builder);
        this.tenantId = builder.tenantId;
        this.supplierId = builder.supplierId;
        this.status = builder.status != null ? builder.status : OrderStatus.CREATED;
        this.notes = builder.notes;
        this.items = builder.items != null ? new ArrayList<>(builder.items) : new ArrayList<>();
        this.totalExcludingTax = calculateTotalExcludingTax();
        this.totalTTC = builder.totalTTC != null ? builder.totalTTC : this.totalExcludingTax;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
    }

    // ── Lifecycle transitions ──────────────────────────────────────────────────

    public void markAsSent() {
        if (this.status != OrderStatus.CREATED) {
            throw new InvalidOrderStatusTransitionException(
                    "Cannot transition from " + this.status + " to SENT. Order must be in CREATED state.");
        }
        this.status = OrderStatus.SENT;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsDelivered() {
        if (this.status != OrderStatus.SENT) {
            throw new InvalidOrderStatusTransitionException(
                    "Cannot transition from " + this.status + " to DELIVERED. Order must be in SENT state.");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == OrderStatus.DELIVERED || this.status == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusTransitionException(
                    "Cannot cancel an order that is already " + this.status);
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    // ── Read-only item access ─────────────────────────────────────────────────

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private BigDecimal calculateTotalExcludingTax() {
        return items.stream()
                .map(OrderItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validate(Builder builder) {
        if (builder.tenantId == null || builder.tenantId.isBlank()) {
            throw new IllegalArgumentException("Order tenantId cannot be empty");
        }
        if (builder.supplierId == null) {
            throw new IllegalArgumentException("Order supplierId cannot be null");
        }
        if (builder.items == null || builder.items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        private UUID id;
        private String tenantId;
        private UUID supplierId;
        private OrderStatus status;
        private String notes;
        private List<OrderItem> items = new ArrayList<>();
        private BigDecimal totalTTC;
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

        public Builder supplierId(UUID supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public Builder totalTTC(BigDecimal totalTTC) {
            this.totalTTC = totalTTC;
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

        public Order build() {
            return new Order(this);
        }
    }
}
