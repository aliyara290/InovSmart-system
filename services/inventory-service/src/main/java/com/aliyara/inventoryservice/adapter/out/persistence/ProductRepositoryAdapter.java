package com.aliyara.inventoryservice.adapter.out.persistence;

import com.aliyara.inventoryservice.adapter.out.persistence.jpa.ProductJpaRepository;
import com.aliyara.inventoryservice.adapter.out.persistence.mapper.ProductPersistenceMapper;
import com.aliyara.inventoryservice.domain.port.ProductRepository;
import com.aliyara.inventoryservice.domain.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    @Override
    public Product save(Product product) {
        var entity = productPersistenceMapper.toEntity(product);
        var saved = productJpaRepository.save(entity);
        return productPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(productPersistenceMapper::toDomain);
    }

    @Override
    public List<Product> findAllByTenantId(String tenantId) {
        return productJpaRepository.findAllByTenantId(tenantId)
                .stream()
                .map(productPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsBySku(String sku, String tenantId) {
        return productJpaRepository.existsBySkuAndTenantId(sku, tenantId);
    }

    @Override
    public void deleteById(UUID id) {
        productJpaRepository.deleteById(id);
    }
}
