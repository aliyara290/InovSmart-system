package com.aliyara.procurementservice.adapter.in.rest;

import com.aliyara.procurementservice.application.dto.supplier.CreateSupplierRequest;
import com.aliyara.procurementservice.application.dto.supplier.SupplierResponse;
import com.aliyara.procurementservice.application.port.in.SupplierUseCase;
import com.aliyara.procurementservice.domain.models.supplier.enums.SupplierStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierUseCase supplierUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        return supplierUseCase.createSupplier(request);
    }

    @GetMapping("/{id}")
    public SupplierResponse getSupplier(@PathVariable UUID id) {
        return supplierUseCase.getSupplier(id);
    }

    @GetMapping
    public List<SupplierResponse> getAllSuppliers(@RequestParam String tenantId) {
        return supplierUseCase.getAllSuppliers(tenantId);
    }

    @PatchMapping("/{id}/status")
    public SupplierResponse updateStatus(@PathVariable UUID id,
            @RequestParam SupplierStatus status) {
        return supplierUseCase.updateSupplierStatus(id, status);
    }
}
