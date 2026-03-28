package com.aliyara.supplyservice.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(nullable = false)
    private String status;

    private String notes;

    @Column(name = "total_excluding_tax", precision = 19, scale = 2)
    private BigDecimal totalExcludingTax;

    @Column(name = "total_ttc", precision = 19, scale = 2)
    private BigDecimal totalTTC;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderItemEntity> items = new ArrayList<>();
}
