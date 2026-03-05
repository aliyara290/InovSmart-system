package com.aliyara.inventoryservice.application.dto.stock;

import com.aliyara.inventoryservice.domain.stock.enums.StockStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StockResponse {
    private UUID id;
    private int quantityTotal;
    private int quantityReserved;
    private int availableQuantity;
    private StockStatus status;
    private int minStock;
    private LocalDateTime lastUpdatedAt;
    private UUID productId;
}
