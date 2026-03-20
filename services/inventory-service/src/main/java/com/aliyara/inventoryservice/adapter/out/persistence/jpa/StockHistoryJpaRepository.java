package com.aliyara.inventoryservice.adapter.out.persistence.jpa;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.StockHistoryEntity;
import com.aliyara.inventoryservice.domain.stock.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockHistoryJpaRepository extends JpaRepository<StockHistoryEntity, UUID> {
    List<StockHistoryEntity> findAllByProductId(UUID productId);

    List<StockHistoryEntity> findAllByProductIdAndTenantId(UUID productId, String tenantId);

    List<StockHistoryEntity> findAllByProductIdAndMovementType(UUID productId, MovementType movementType);

    @Query("SELECT sh FROM StockHistoryEntity sh WHERE sh.tenantId = :tenantId AND sh.id IN " +
            "(SELECT sh2.id FROM StockHistoryEntity sh2)")
    List<StockHistoryEntity> findAllByTenantId(String tenantId);

    @Query("SELECT sh FROM StockHistoryEntity sh WHERE sh.tenantId = :tenantId")
    List<StockHistoryEntity> findByTenantIdAndDateRange(@Param("tenantId") String tenantId,
                                                        @Param("from") LocalDateTime from,
                                                        @Param("to") LocalDateTime to);
}
