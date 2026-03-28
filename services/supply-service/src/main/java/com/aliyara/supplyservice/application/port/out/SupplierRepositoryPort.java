package com.aliyara.supplyservice.application.port.out;

import com.aliyara.supplyservice.domain.model.supplier.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepositoryPort {
    Supplier save(Supplier supplier);
    Optional<Supplier> findById(UUID id);
    List<Supplier> findAllByTenantId(String tenantId);
    void deleteById(UUID id);
    boolean existsByTenantIdAndName(String tenantId, String name);
}
