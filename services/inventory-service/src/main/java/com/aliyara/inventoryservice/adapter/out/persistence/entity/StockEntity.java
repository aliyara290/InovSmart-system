package com.aliyara.inventoryservice.adapter.out.persistence.entity;

import com.aliyara.inventoryservice.domain.model.stock.enums.StockStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "quantity_total", nullable = false)
    private int quantityTotal;

    @Column(name = "quantity_reserved", nullable = false)
    private int quantityReserved;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockStatus status;

    @Column(name = "min_stock", nullable = false)
    private int minStock;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;
}
