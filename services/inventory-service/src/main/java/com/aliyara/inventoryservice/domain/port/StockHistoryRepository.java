package com.aliyara.inventoryservice.domain.port;

import com.aliyara.inventoryservice.domain.model.stock.StockHistory;
import com.aliyara.inventoryservice.domain.model.stock.enums.MovementType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockHistoryRepository {

    StockHistory save(StockHistory stockHistory);

    List<StockHistory> findByProductId(UUID productId);

    List<StockHistory> findByProductIdAndTenantId(UUID productId, String tenantId);

    List<StockHistory> findByProductIdAndMovementType(UUID productId, MovementType movementType);

    List<StockHistory> findByTenantIdAndDateRange(String tenantId, LocalDateTime from, LocalDateTime to);
}
