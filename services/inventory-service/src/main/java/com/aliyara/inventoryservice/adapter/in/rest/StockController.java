package com.aliyara.inventoryservice.adapter.in.rest;

import com.aliyara.inventoryservice.application.dto.stock.AdjustStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.ReserveStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.StockResponse;
import com.aliyara.inventoryservice.application.port.in.StockUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockUseCase stockUseCase;

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public StockResponse getStock(@PathVariable UUID productId) {
        return stockUseCase.getStockByProductId(productId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public List<StockResponse> getAllStocks() {
        return stockUseCase.getAllStocks();
    }

    @PutMapping("/product/{productId}/adjust")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public StockResponse adjustQuantity(@PathVariable UUID productId,
            @Valid @RequestBody AdjustStockRequest request) {
        return stockUseCase.adjustQuantity(productId, request);
    }

    @PutMapping("/product/{productId}/reserve")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public StockResponse reserveStock(@PathVariable UUID productId,
            @Valid @RequestBody ReserveStockRequest request) {
        log.debug("Billing request: {}", request);
        return stockUseCase.reserveStock(productId, request);
    }

    @PutMapping("/product/{productId}/release")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public StockResponse releaseReservation(@PathVariable UUID productId,
            @Valid @RequestBody ReserveStockRequest request) {
        return stockUseCase.releaseReservation(productId, request);
    }
}
