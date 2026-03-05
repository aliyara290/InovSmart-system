package com.aliyara.inventoryservice.adapter.out.persistence.entity;

import com.aliyara.inventoryservice.domain.stock.enums.MovementType;
import com.aliyara.inventoryservice.domain.stock.enums.ReferenceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "stock_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockHistoryEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column(name = "quantity_change", nullable = false)
    private int quantityChange;

    @Column(name = "quantity_before", nullable = false)
    private int quantityBefore;

    @Column(name = "quantity_after", nullable = false)
    private int quantityAfter;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "performed_by", nullable = false)
    private UUID performedBy;
}
