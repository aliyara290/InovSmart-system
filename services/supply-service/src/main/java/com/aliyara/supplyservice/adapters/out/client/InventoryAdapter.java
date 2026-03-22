package com.aliyara.supplyservice.adapters.out.client;

import com.aliyara.supplyservice.application.dto.inventory.AdjustStockRequest;
import com.aliyara.supplyservice.application.dto.inventory.ProductInfo;
import com.aliyara.supplyservice.application.dto.inventory.StockInfo;
import com.aliyara.supplyservice.application.port.out.InventoryPort;
import com.aliyara.supplyservice.domain.exception.ProductNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryAdapter implements InventoryPort {

    private final InventoryServiceClient inventoryServiceClient;

    @Override
    public ProductInfo getProduct(UUID productId) {
        try {
            return inventoryServiceClient.getProduct(productId);
        } catch (FeignException.NotFound e) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
    }

    @Override
    public StockInfo getStockByProductId(UUID productId) {
        try {
            return inventoryServiceClient.getStockByProductId(productId);
        } catch (FeignException.NotFound e) {
            log.warn("Stock not found for product: {}", productId);
            return null;
        }
    }

    @Override
    public List<StockInfo> getAllStocks() {
        return inventoryServiceClient.getAllStocks();
    }

    @Override
    public void adjustStock(UUID productId, int newQuantity, UUID orderId, UUID performedBy) {
        AdjustStockRequest request = AdjustStockRequest.builder()
                .newQuantityTotal(newQuantity)
                .reason("Purchase order delivery")
                .referenceType("PURCHASE")
                .referenceId(orderId)
                .performedBy(performedBy)
                .build();
        inventoryServiceClient.adjustStock(productId, request);
    }
}
