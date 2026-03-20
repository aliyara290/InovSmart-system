package com.aliyara.inventoryservice.adapter.out.persistence.jpa;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findAllByTenantId(String tenantId);

    Optional<CategoryEntity> findByIdAndTenantId(UUID id, String tenantId);

    Optional<CategoryEntity> findByNameAndTenantId(String name, String tenantId);

    boolean existsByNameAndTenantId(String name, String tenantId);
}
