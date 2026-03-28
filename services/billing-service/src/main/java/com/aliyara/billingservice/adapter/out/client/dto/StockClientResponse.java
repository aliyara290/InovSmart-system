package com.aliyara.billingservice.adapter.out.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StockClientResponse {
    private UUID id;
    private int quantityTotal;
    private int quantityReserved;
    private int availableQuantity;
    private String status;
    private int minStock;
    private LocalDateTime lastUpdatedAt;
    private UUID productId;
}
