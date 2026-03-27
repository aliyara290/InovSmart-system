package com.aliyara.billingservice.application.port.out;

import java.util.UUID;

public interface InventoryServicePort {

    boolean productExists(UUID productId);

    String getProductName(UUID productId);

    int getAvailableStock(UUID productId);

    void reserveStock(UUID productId, int quantity, UUID referenceId);

    void releaseReservation(UUID productId, int quantity, UUID referenceId);

    void adjustStock(UUID productId, int newQuantityTotal, UUID referenceId);
}
