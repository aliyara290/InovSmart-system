package com.aliyara.billingservice.adapter.out.client;

import com.aliyara.billingservice.adapter.out.client.dto.AdjustStockClientRequest;
import com.aliyara.billingservice.adapter.out.client.dto.ProductClientResponse;
import com.aliyara.billingservice.adapter.out.client.dto.ReserveStockClientRequest;
import com.aliyara.billingservice.adapter.out.client.dto.StockClientResponse;
import com.aliyara.billingservice.application.port.out.InventoryServicePort;
import com.aliyara.billingservice.domain.exception.ProductNotFoundException;
import com.aliyara.billingservice.domain.exception.StockReservationException;
import com.aliyara.billingservice.infrastructure.config.TenantContextHolder;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryServiceAdapter implements InventoryServicePort {

    private static final String INVENTORY_SERVICE = "inventoryService";
    private static final UUID SYSTEM_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final InventoryFeignClient inventoryFeignClient;

    @Override
    @CircuitBreaker(name = INVENTORY_SERVICE)
    @Retry(name = INVENTORY_SERVICE)
    public boolean productExists(UUID productId) {
        try {
            inventoryFeignClient.getProduct(productId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            log.error("Error checking product existence for productId: {}", productId, e);
            throw new ProductNotFoundException("Unable to verify product: " + productId);
        }
    }

    @Override
    @CircuitBreaker(name = INVENTORY_SERVICE)
    @Retry(name = INVENTORY_SERVICE)
    public String getProductName(UUID productId) {
        try {
            ProductClientResponse product = inventoryFeignClient.getProduct(productId);
            return product.getName();
        } catch (Exception e) {
            log.warn("Could not fetch product name for productId: {}", productId, e);
            return null;
        }
    }

    @Override
    @CircuitBreaker(name = INVENTORY_SERVICE)
    @Retry(name = INVENTORY_SERVICE)
    public int getAvailableStock(UUID productId) {
        try {
            StockClientResponse stock = inventoryFeignClient.getStock(productId);
            return stock.getAvailableQuantity();
        } catch (Exception e) {
            log.error("Error fetching stock for productId: {}", productId, e);
            throw new StockReservationException("Unable to fetch stock for product: " + productId, e);
        }
    }

    @Override
    @CircuitBreaker(name = INVENTORY_SERVICE)
    @Retry(name = INVENTORY_SERVICE)
    public void reserveStock(UUID productId, int quantity, UUID referenceId) {
        try {
            ReserveStockClientRequest request = ReserveStockClientRequest.builder()
                    .quantity(quantity)
                    .reason("Stock reserved for quote acceptance")
                    .referenceType("ORDER")
                    .referenceId(referenceId)
                    .performedBy(SYSTEM_USER_ID)
                    .build();
            inventoryFeignClient.reserveStock(productId, request);
        } catch (Exception e) {
            log.error("Error reserving stock for productId: {}, quantity: {}", productId, quantity, e);
            throw new StockReservationException("Failed to reserve stock for product: " + productId, e);
        }
    }

    @Override
    @CircuitBreaker(name = INVENTORY_SERVICE)
    @Retry(name = INVENTORY_SERVICE)
    public void releaseReservation(UUID productId, int quantity, UUID referenceId) {
        try {
            ReserveStockClientRequest request = ReserveStockClientRequest.builder()
                    .quantity(quantity)
                    .reason("Reservation released due to quote rejection")
                    .referenceType("ORDER")
                    .referenceId(referenceId)
                    .performedBy(SYSTEM_USER_ID)
                    .build();
            inventoryFeignClient.releaseReservation(productId, request);
        } catch (Exception e) {
            log.error("Error releasing reservation for productId: {}, quantity: {}", productId, quantity, e);
            throw new StockReservationException("Failed to release reservation for product: " + productId, e);
        }
    }

    @Override
    @CircuitBreaker(name = INVENTORY_SERVICE)
    @Retry(name = INVENTORY_SERVICE)
    public void adjustStock(UUID productId, int newQuantityTotal, UUID referenceId) {
        try {
            AdjustStockClientRequest request = AdjustStockClientRequest.builder()
                    .newQuantityTotal(newQuantityTotal)
                    .reason("Stock adjusted for invoice creation")
                    .referenceType("ORDER")
                    .referenceId(referenceId)
                    .performedBy(SYSTEM_USER_ID)
                    .build();
            inventoryFeignClient.adjustQuantity(productId, request);
        } catch (Exception e) {
            log.error("Error adjusting stock for productId: {}, newQuantityTotal: {}", productId, newQuantityTotal, e);
            throw new StockReservationException("Failed to adjust stock for product: " + productId, e);
        }
    }
}
