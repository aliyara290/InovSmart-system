package com.aliyara.supplyservice.application.service;

import com.aliyara.supplyservice.application.dto.supplier.CreateSupplierRequest;
import com.aliyara.supplyservice.application.dto.supplier.SupplierResponse;
import com.aliyara.supplyservice.application.dto.supplier.UpdateSupplierRequest;
import com.aliyara.supplyservice.application.port.in.SupplierUseCase;
import com.aliyara.supplyservice.application.port.out.SupplierRepositoryPort;
import com.aliyara.supplyservice.domain.exception.SupplierNotFoundException;
import com.aliyara.supplyservice.domain.model.supplier.Supplier;
import com.aliyara.supplyservice.infrastrecture.config.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService implements SupplierUseCase {

    private final SupplierRepositoryPort supplierRepository;

    @Override
    @Transactional
    public SupplierResponse createSupplier(CreateSupplierRequest request) {
        String tenantId = TenantContextHolder.getTenantIdAsString();

        Supplier supplier = new Supplier.Builder()
                .tenantId(tenantId)
                .name(request.getName())
                .contactEmail(request.getContactEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        Supplier saved = supplierRepository.save(supplier);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplier(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
        return toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        String tenantId = TenantContextHolder.getTenantIdAsString();
        return supplierRepository.findAllByTenantId(tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(UUID id, UpdateSupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));

        supplier.updateDetails(
                request.getName(),
                request.getContactEmail(),
                request.getPhone(),
                request.getAddress()
        );

        Supplier saved = supplierRepository.save(supplier);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteSupplier(UUID id) {
        supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
        supplierRepository.deleteById(id);
    }

    private SupplierResponse toResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactEmail(supplier.getContactEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .status(supplier.getStatus().name())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }
}
