package com.aliyara.procurementservice.application.service;

import com.aliyara.procurementservice.application.dto.supplier.CreateSupplierRequest;
import com.aliyara.procurementservice.application.dto.supplier.SupplierResponse;
import com.aliyara.procurementservice.application.mapper.SupplierDtoMapper;
import com.aliyara.procurementservice.application.port.in.SupplierUseCase;
import com.aliyara.procurementservice.domain.exception.SupplierNotFoundException;
import com.aliyara.procurementservice.domain.models.supplier.Supplier;
import com.aliyara.procurementservice.domain.models.supplier.enums.SupplierStatus;
import com.aliyara.procurementservice.domain.port.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierService implements SupplierUseCase {

    private final SupplierRepository supplierRepository;
    private final SupplierDtoMapper supplierDtoMapper;

    @Override
    @Transactional
    public SupplierResponse createSupplier(CreateSupplierRequest request) {
        log.info("Creating supplier for tenantId={}, name={}", request.getTenantId(), request.getName());
        Supplier supplier = supplierDtoMapper.toDomain(request);
        Supplier saved = supplierRepository.save(supplier);
        log.info("Supplier created with id={}", saved.getId());
        return supplierDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplier(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
        return supplierDtoMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers(String tenantId) {
        return supplierRepository.findAllByTenantId(tenantId)
                .stream()
                .map(supplierDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplierStatus(UUID id, SupplierStatus newStatus) {
        log.info("Updating supplier id={} status to {}", id, newStatus);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
        supplier.updateStatus(newStatus);
        return supplierDtoMapper.toResponse(supplierRepository.save(supplier));
    }
}
