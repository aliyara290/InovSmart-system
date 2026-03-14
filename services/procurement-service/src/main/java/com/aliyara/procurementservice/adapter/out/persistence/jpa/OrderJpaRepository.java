package com.aliyara.procurementservice.adapter.out.persistence.jpa;

import com.aliyara.procurementservice.adapter.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByTenantId(String tenantId);
}
