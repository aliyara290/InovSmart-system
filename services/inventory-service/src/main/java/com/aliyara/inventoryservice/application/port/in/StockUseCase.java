package com.aliyara.inventoryservice.application.port.in;

import com.aliyara.inventoryservice.application.dto.stock.AdjustStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.ReserveStockRequest;
import com.aliyara.inventoryservice.application.dto.stock.StockResponse;

import java.util.List;
import java.util.UUID;

public interface StockUseCase {
    StockResponse getStockByProductId(UUID productId);

    List<StockResponse> getAllStocks(String tenantId);

    StockResponse adjustQuantity(UUID productId, AdjustStockRequest request);

    StockResponse reserveStock(UUID productId, ReserveStockRequest request);

    StockResponse releaseReservation(UUID productId, ReserveStockRequest request);
}
