package com.aliyara.inventoryservice.domain.port;

import com.aliyara.inventoryservice.domain.model.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAllByTenantId(String tenantId);

    List<Product> findByCategoryIdAndTenantId(UUID categoryId, String tenantId);

    boolean existsBySku(String sku, String tenantId);

    void deleteById(UUID id);
}
