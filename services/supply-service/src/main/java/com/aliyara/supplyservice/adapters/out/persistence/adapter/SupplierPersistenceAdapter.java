package com.aliyara.supplyservice.adapters.out.persistence.adapter;

import com.aliyara.supplyservice.adapters.out.persistence.jpa.SupplierJpaRepository;
import com.aliyara.supplyservice.adapters.out.persistence.mapper.SupplierPersistenceMapper;
import com.aliyara.supplyservice.application.port.out.SupplierRepositoryPort;
import com.aliyara.supplyservice.domain.model.supplier.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupplierPersistenceAdapter implements SupplierRepositoryPort {

    private final SupplierJpaRepository repository;
    private final SupplierPersistenceMapper mapper;

    @Override
    public Supplier save(Supplier supplier) {
        return mapper.toDomain(repository.save(mapper.toEntity(supplier)));
    }

    @Override
    public Optional<Supplier> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Supplier> findAllByTenantId(String tenantId) {
        return repository.findAllByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByTenantIdAndName(String tenantId, String name) {
        return repository.existsByTenantIdAndName(tenantId, name);
    }
}
