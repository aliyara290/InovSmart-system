package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;
import com.aliyara.inventoryservice.application.mapper.ProductDtoMapper;
import com.aliyara.inventoryservice.application.port.in.ProductUseCase;
import com.aliyara.inventoryservice.domain.exception.DuplicateSkuException;
import com.aliyara.inventoryservice.domain.exception.ProductNotFoundException;
import com.aliyara.inventoryservice.domain.port.ProductRepository;
import com.aliyara.inventoryservice.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku(), request.getTenantId())) {
            throw new DuplicateSkuException(
                    "Product with SKU '" + request.getSku() + "' already exists for this tenant");
        }
        Product product = productDtoMapper.toDomain(request);
        Product saved = productRepository.save(product);
        return productDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productDtoMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(String tenantId) {
        return productRepository.findAllByTenantId(tenantId)
                .stream()
                .map(productDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        existing.updateDetails(request.getName(), request.getDescription(), request.getPrice());
        existing.updateSku(request.getSku());
        Product saved = productRepository.save(existing);
        return productDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepository.deleteById(id);
    }
}
