package com.aliyara.procurementservice.adapter.out.persistence;

import com.aliyara.procurementservice.adapter.out.persistence.jpa.SupplierJpaRepository;
import com.aliyara.procurementservice.adapter.out.persistence.mapper.SupplierPersistenceMapper;
import com.aliyara.procurementservice.domain.models.supplier.Supplier;
import com.aliyara.procurementservice.domain.port.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupplierRepositoryAdapter implements SupplierRepository {

    private final SupplierJpaRepository supplierJpaRepository;
    private final SupplierPersistenceMapper supplierPersistenceMapper;

    @Override
    public Supplier save(Supplier supplier) {
        var entity = supplierPersistenceMapper.toEntity(supplier);
        var saved = supplierJpaRepository.save(entity);
        return supplierPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Supplier> findById(UUID id) {
        return supplierJpaRepository.findById(id)
                .map(supplierPersistenceMapper::toDomain);
    }

    @Override
    public List<Supplier> findAllByTenantId(String tenantId) {
        return supplierJpaRepository.findAllByTenantId(tenantId)
                .stream()
                .map(supplierPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndTenantId(String name, String tenantId) {
        return supplierJpaRepository.existsByNameAndTenantId(name, tenantId);
    }
}
