package com.aliyara.billingservice.adapter.out.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductClientResponse {
    private UUID id;
    private String tenantId;
    private String name;
    private String description;
    private double price;
    private String sku;
    private UUID productId;
    private UUID categoryId;
}
