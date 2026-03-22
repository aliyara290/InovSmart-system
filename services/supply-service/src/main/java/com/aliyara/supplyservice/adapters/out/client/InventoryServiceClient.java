package com.aliyara.supplyservice.adapters.out.client;

import com.aliyara.supplyservice.application.dto.inventory.AdjustStockRequest;
import com.aliyara.supplyservice.application.dto.inventory.ProductInfo;
import com.aliyara.supplyservice.application.dto.inventory.StockInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "inventory-service")
public interface InventoryServiceClient {

    @GetMapping("/api/v1/products/{id}")
    ProductInfo getProduct(@PathVariable("id") UUID id);

    @GetMapping("/api/v1/stocks/product/{productId}")
    StockInfo getStockByProductId(@PathVariable("productId") UUID productId);

    @GetMapping("/api/v1/stocks")
    List<StockInfo> getAllStocks();

    @PutMapping("/api/v1/stocks/product/{productId}/adjust")
    StockInfo adjustStock(@PathVariable("productId") UUID productId, @RequestBody AdjustStockRequest request);
}
