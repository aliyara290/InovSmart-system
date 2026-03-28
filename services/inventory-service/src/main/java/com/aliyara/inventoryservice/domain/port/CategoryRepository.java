package com.aliyara.inventoryservice.domain.port;

import com.aliyara.inventoryservice.domain.model.category.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(UUID id);

    Optional<Category> findByIdAndTenantId(UUID id, String tenantId);

    Optional<Category> findByNameAndTenantId(String name, String tenantId);

    List<Category> findAllByTenantId(String tenantId);

    void deleteById(UUID id);

    boolean existsByNameAndTenantId(String name, String tenantId);
}
