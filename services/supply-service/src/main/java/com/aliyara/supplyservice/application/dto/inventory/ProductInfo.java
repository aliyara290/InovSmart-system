package com.aliyara.supplyservice.application.dto.inventory;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfo {
    private UUID id;
    private String tenantId;
    private String name;
    private String description;
    private double price;
    private String sku;
    private UUID productId;
    private UUID categoryId;
}
