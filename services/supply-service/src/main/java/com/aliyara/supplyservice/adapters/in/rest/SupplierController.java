package com.aliyara.supplyservice.adapters.in.rest;

import com.aliyara.supplyservice.application.dto.supplier.CreateSupplierRequest;
import com.aliyara.supplyservice.application.dto.supplier.SupplierResponse;
import com.aliyara.supplyservice.application.dto.supplier.UpdateSupplierRequest;
import com.aliyara.supplyservice.application.port.in.SupplierUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierUseCase supplierUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        SupplierResponse response = supplierUseCase.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<SupplierResponse> getSupplier(@PathVariable UUID id) {
        return ResponseEntity.ok(supplierUseCase.getSupplier(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(supplierUseCase.getAllSuppliers());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable UUID id,
                                                            @Valid @RequestBody UpdateSupplierRequest request) {
        return ResponseEntity.ok(supplierUseCase.updateSupplier(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {
        supplierUseCase.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
