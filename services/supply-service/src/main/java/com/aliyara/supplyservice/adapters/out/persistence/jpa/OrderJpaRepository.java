package com.aliyara.supplyservice.adapters.out.persistence.jpa;

import com.aliyara.supplyservice.adapters.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByTenantId(String tenantId);
    List<OrderEntity> findAllByTenantIdAndSupplierId(String tenantId, UUID supplierId);
    Optional<OrderEntity> findTopByTenantIdOrderByOrderNumberDesc(String tenantId);
}
