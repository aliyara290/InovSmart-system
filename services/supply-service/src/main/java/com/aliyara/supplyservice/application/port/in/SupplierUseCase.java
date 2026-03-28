package com.aliyara.supplyservice.application.port.in;

import com.aliyara.supplyservice.application.dto.supplier.CreateSupplierRequest;
import com.aliyara.supplyservice.application.dto.supplier.SupplierResponse;
import com.aliyara.supplyservice.application.dto.supplier.UpdateSupplierRequest;

import java.util.List;
import java.util.UUID;

public interface SupplierUseCase {
    SupplierResponse createSupplier(CreateSupplierRequest request);
    SupplierResponse getSupplier(UUID id);
    List<SupplierResponse> getAllSuppliers();
    SupplierResponse updateSupplier(UUID id, UpdateSupplierRequest request);
    void deleteSupplier(UUID id);
}
