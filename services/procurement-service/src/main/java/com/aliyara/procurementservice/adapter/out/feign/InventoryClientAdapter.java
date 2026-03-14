package com.aliyara.procurementservice.adapter.out.feign;

import com.aliyara.procurementservice.application.dto.feign.ProductResponse;
import com.aliyara.procurementservice.application.port.out.InventoryClientPort;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryClientAdapter implements InventoryClientPort {

    private final InventoryClient inventoryClient;

    @Override
    public boolean productExists(UUID productId) {
        try {
            ProductResponse product = inventoryClient.getProduct(productId);
            boolean exists = product != null && product.isActive();
            log.debug("Product {} exists and active: {}", productId, exists);
            return exists;
        } catch (FeignException.NotFound e) {
            log.debug("Product {} not found in Inventory Service", productId);
            return false;
        } catch (FeignException e) {
            log.error("Feign error while checking product {}: {} - {}", productId, e.status(), e.getMessage());
            throw new RuntimeException("Inventory Service is unavailable. Unable to validate product: " + productId, e);
        }
    }
}
