package com.aliyara.inventoryservice.application.port.in;

import com.aliyara.inventoryservice.application.dto.stock.StockHistoryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockHistoryUseCase {

    List<StockHistoryResponse> getHistoryByProductId(UUID productId, String tenantId);

    List<StockHistoryResponse> getHistoryByDateRange(String tenantId, LocalDateTime from, LocalDateTime to);
}
