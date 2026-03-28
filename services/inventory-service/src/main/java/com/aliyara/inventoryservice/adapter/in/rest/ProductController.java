package com.aliyara.inventoryservice.adapter.in.rest;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;
import com.aliyara.inventoryservice.application.port.in.ProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productUseCase.createProduct(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public ProductResponse getProduct(@PathVariable UUID id) {
        return productUseCase.getProduct(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'ACCOUNTANT', 'EMPLOYEE')")
    public List<ProductResponse> getAllProducts() {
        return productUseCase.getAllProducts();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ProductResponse updateProduct(@PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        return productUseCase.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public void deleteProduct(@PathVariable UUID id) {
        productUseCase.deleteProduct(id);
    }
}
