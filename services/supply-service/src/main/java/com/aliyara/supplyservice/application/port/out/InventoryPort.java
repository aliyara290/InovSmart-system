package com.aliyara.supplyservice.application.port.out;

import com.aliyara.supplyservice.application.dto.inventory.ProductInfo;
import com.aliyara.supplyservice.application.dto.inventory.StockInfo;

import java.util.List;
import java.util.UUID;

public interface InventoryPort {
    ProductInfo getProduct(UUID productId);
    StockInfo getStockByProductId(UUID productId);
    List<StockInfo> getAllStocks();
    void adjustStock(UUID productId, int newQuantity, UUID orderId, UUID performedBy);
//    StockInfo reserveStock(UUID productId, int quantity, String reason, String referenceType, UUID referenceId, UUID performedBy);
//    StockInfo releaseReservation(UUID productId, int quantity, String reason, String referenceType, UUID referenceId, UUID performedBy);
}
