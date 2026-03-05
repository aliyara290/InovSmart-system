package com.aliyara.inventoryservice.application.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @Positive(message = "price must be positive")
    private double price;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "categoryId is required")
    private UUID categoryId;
}
