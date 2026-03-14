package com.aliyara.procurementservice.domain.port;

import com.aliyara.procurementservice.domain.models.supplier.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository {
    Supplier save(Supplier supplier);

    Optional<Supplier> findById(UUID id);

    List<Supplier> findAllByTenantId(String tenantId);

    boolean existsByNameAndTenantId(String name, String tenantId);
}
