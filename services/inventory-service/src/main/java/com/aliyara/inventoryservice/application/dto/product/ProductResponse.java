package com.aliyara.inventoryservice.application.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {
    private UUID id;
    private String tenantId;
    private String name;
    private String description;
    private double price;
    private String sku;
    private UUID productId;
    private UUID categoryId;
}
