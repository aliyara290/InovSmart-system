package com.aliyara.inventoryservice.adapter.out.persistence.jpa;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockJpaRepository extends JpaRepository<StockEntity, UUID> {
    Optional<StockEntity> findByProductId(UUID productId);

    @Query("SELECT s FROM StockEntity s WHERE s.productId IN " +
            "(SELECT p.id FROM ProductEntity p WHERE p.tenantId = :tenantId)")
    List<StockEntity> findAllByTenantId(@Param("tenantId") String tenantId);

    @Query("SELECT s FROM StockEntity s WHERE s.productId = :productId AND s.productId IN " +
            "(SELECT p.id FROM ProductEntity p WHERE p.tenantId = :tenantId)")
    Optional<StockEntity> findByProductIdAndTenantId(@Param("productId") UUID productId,
                                                     @Param("tenantId") String tenantId);

    @Query("SELECT s FROM StockEntity s WHERE s.status = :status AND s.productId IN " +
            "(SELECT p.id FROM ProductEntity p WHERE p.tenantId = :tenantId)")
    List<StockEntity> findByTenantIdAndStatus(@Param("tenantId") String tenantId,
                                              @Param("status") com.aliyara.inventoryservice.domain.stock.enums.StockStatus status);

    @Query("SELECT s FROM StockEntity s WHERE s.status = 'LOW_STOCK' AND s.productId IN " +
            "(SELECT p.id FROM ProductEntity p WHERE p.tenantId = :tenantId)")
    List<StockEntity> findLowStockByTenantId(@Param("tenantId") String tenantId);

    void deleteByProductId(UUID productId);
}
