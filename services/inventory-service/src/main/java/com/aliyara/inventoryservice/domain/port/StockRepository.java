package com.aliyara.inventoryservice.domain.port;

import com.aliyara.inventoryservice.domain.stock.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {
    Stock save(Stock stock);

    Optional<Stock> findById(UUID id);

    Optional<Stock> findByProductId(UUID productId);

    List<Stock> findAllByTenantId(String tenantId);
}
