package com.aliyara.supplyservice.application.dto.inventory;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInfo {
    private UUID id;
    private int quantityTotal;
    private int quantityReserved;
    private int availableQuantity;
    private String status;
    private int minStock;
    private LocalDateTime lastUpdatedAt;
    private UUID productId;
}
