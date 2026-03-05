package com.aliyara.inventoryservice.domain.port;

import com.aliyara.inventoryservice.domain.stock.StockHistory;

import java.util.List;
import java.util.UUID;

public interface StockHistoryRepository {
    StockHistory save(StockHistory stockHistory);

    List<StockHistory> findAllByProductId(UUID productId);

    List<StockHistory> findAllByTenantId(String tenantId);
}
