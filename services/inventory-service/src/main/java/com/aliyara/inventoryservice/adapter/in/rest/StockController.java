package com.aliyara.inventoryservice.adapter.in.rest;

import com.aliyara.inventoryservice.application.dto.stock.AdjustStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.ReserveStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.StockResponse;
import com.aliyara.inventoryservice.application.port.in.StockUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockUseCase stockUseCase;

    @GetMapping("/product/{productId}")
    public StockResponse getStock(@PathVariable UUID productId) {
        return stockUseCase.getStockByProductId(productId);
    }

    @GetMapping
    public List<StockResponse> getAllStocks(@RequestParam String tenantId) {
        return stockUseCase.getAllStocks(tenantId);
    }

    @PutMapping("/product/{productId}/adjust")
    public StockResponse adjustQuantity(@PathVariable UUID productId,
            @Valid @RequestBody AdjustStockRequest request) {
        return stockUseCase.adjustQuantity(productId, request);
    }

    @PutMapping("/product/{productId}/reserve")
    public StockResponse reserveStock(@PathVariable UUID productId,
            @Valid @RequestBody ReserveStockRequest request) {
        return stockUseCase.reserveStock(productId, request);
    }

    @PutMapping("/product/{productId}/release")
    public StockResponse releaseReservation(@PathVariable UUID productId,
            @Valid @RequestBody ReserveStockRequest request) {
        return stockUseCase.releaseReservation(productId, request);
    }
}
