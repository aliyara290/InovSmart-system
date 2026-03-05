package com.aliyara.inventoryservice.domain.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID productId) {
        super("Product not found with id: " + productId);
    }

    public ProductNotFoundException(String sku) {
        super("Product not found with SKU: " + sku);
    }

    public ProductNotFoundException(UUID productId, String tenantId) {
        super(String.format("Product not found with id: %s for tenant: %s", productId, tenantId));
    }
}
