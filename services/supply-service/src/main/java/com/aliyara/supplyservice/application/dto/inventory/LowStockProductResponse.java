package com.aliyara.supplyservice.application.dto.inventory;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LowStockProductResponse {
    private UUID productId;
    private String productName;
    private int currentStock;
    private int minStock;
    private String stockStatus;
}
