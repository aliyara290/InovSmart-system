package com.aliyara.inventoryservice.application.port.in;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductUseCase {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProduct(UUID id);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(UUID id, ProductRequest request);

    void deleteProduct(UUID id);
}
