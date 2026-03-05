package com.aliyara.inventoryservice.adapter.out.persistence.jpa;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findAllByTenantId(String tenantId);

    boolean existsBySkuAndTenantId(String sku, String tenantId);
}
