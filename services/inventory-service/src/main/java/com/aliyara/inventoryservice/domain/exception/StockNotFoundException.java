package com.aliyara.inventoryservice.domain.exception;

import java.util.UUID;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(UUID productId) {
        super("Stock record not found for product id: " + productId);
    }

    public StockNotFoundException(UUID productId, String tenantId) {
        super(String.format("Stock record not found for product id: %s and tenant: %s", productId, tenantId));
    }
}
