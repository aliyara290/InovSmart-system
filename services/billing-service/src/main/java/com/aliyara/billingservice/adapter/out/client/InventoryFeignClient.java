package com.aliyara.billingservice.adapter.out.client;

import com.aliyara.billingservice.adapter.out.client.dto.*;
import com.aliyara.billingservice.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "inventory-service",
        configuration = FeignConfig.class
)
public interface InventoryFeignClient {

    @GetMapping("/api/v1/products/{id}")
    ProductClientResponse getProduct(@PathVariable("id") UUID id);

    @GetMapping("/api/v1/stocks/product/{productId}")
    StockClientResponse getStock(@PathVariable("productId") UUID productId);

    @PutMapping("/api/v1/stocks/product/{productId}/reserve")
    StockClientResponse reserveStock(@PathVariable("productId") UUID productId,
                                     @RequestBody ReserveStockClientRequest request);

    @PutMapping("/api/v1/stocks/product/{productId}/release")
    StockClientResponse releaseReservation(@PathVariable("productId") UUID productId,
                                           @RequestBody ReserveStockClientRequest request);

    @PutMapping("/api/v1/stocks/product/{productId}/adjust")
    StockClientResponse adjustQuantity(@PathVariable("productId") UUID productId,
                                       @RequestBody AdjustStockClientRequest request);
}
