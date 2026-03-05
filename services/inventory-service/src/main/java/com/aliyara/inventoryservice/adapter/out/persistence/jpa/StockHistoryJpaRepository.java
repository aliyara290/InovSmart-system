package com.aliyara.inventoryservice.adapter.out.persistence.jpa;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.StockHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockHistoryJpaRepository extends JpaRepository<StockHistoryEntity, UUID> {
    List<StockHistoryEntity> findAllByProductId(UUID productId);

    List<StockHistoryEntity> findAllByTenantId(String tenantId);
}
