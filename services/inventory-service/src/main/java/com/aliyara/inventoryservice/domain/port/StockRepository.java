package com.aliyara.inventoryservice.domain.port;

import com.aliyara.inventoryservice.domain.model.stock.Stock;
import com.aliyara.inventoryservice.domain.model.stock.enums.StockStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {

    Stock save(Stock stock);

    Optional<Stock> findByProductId(UUID productId);

    Optional<Stock> findByProductIdAndTenantId(UUID productId, String tenantId);

    List<Stock> findAllByTenantId(String tenantId);

    List<Stock> findByTenantIdAndStatus(String tenantId, StockStatus status);

    List<Stock> findLowStockByTenantId(String tenantId);

    void deleteByProductId(UUID productId);
}
