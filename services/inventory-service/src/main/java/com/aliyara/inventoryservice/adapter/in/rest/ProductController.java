package com.aliyara.inventoryservice.adapter.in.rest;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;
import com.aliyara.inventoryservice.application.port.in.ProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productUseCase.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable UUID id) {
        return productUseCase.getProduct(id);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts(@RequestParam String tenantId) {
        return productUseCase.getAllProducts(tenantId);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        return productUseCase.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        productUseCase.deleteProduct(id);
    }
}
