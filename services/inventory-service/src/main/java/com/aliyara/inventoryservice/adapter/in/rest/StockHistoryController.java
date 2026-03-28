package com.aliyara.inventoryservice.adapter.in.rest;

import com.aliyara.inventoryservice.application.dto.stock.StockHistoryResponse;
import com.aliyara.inventoryservice.application.port.in.StockHistoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockHistoryController {
    private final StockHistoryUseCase stockHistoryUseCase;

    @GetMapping("/product/{productId}/history")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT')")
    public List<StockHistoryResponse> getHistory(@PathVariable UUID productId) {
        return stockHistoryUseCase.getHistoryByProductId(productId);
    }
}