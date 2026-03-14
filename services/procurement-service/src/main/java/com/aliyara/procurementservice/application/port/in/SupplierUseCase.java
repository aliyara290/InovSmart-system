package com.aliyara.procurementservice.application.port.in;

import com.aliyara.procurementservice.application.dto.supplier.CreateSupplierRequest;
import com.aliyara.procurementservice.application.dto.supplier.SupplierResponse;
import com.aliyara.procurementservice.domain.models.supplier.enums.SupplierStatus;

import java.util.List;
import java.util.UUID;

public interface SupplierUseCase {
    SupplierResponse createSupplier(CreateSupplierRequest request);

    SupplierResponse getSupplier(UUID id);

    List<SupplierResponse> getAllSuppliers(String tenantId);

    SupplierResponse updateSupplierStatus(UUID id, SupplierStatus status);
}
