package com.aliyara.procurementservice.adapter.out.persistence.jpa;

import com.aliyara.procurementservice.adapter.out.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SupplierJpaRepository extends JpaRepository<SupplierEntity, UUID> {
    List<SupplierEntity> findAllByTenantId(String tenantId);

    boolean existsByNameAndTenantId(String name, String tenantId);
}
