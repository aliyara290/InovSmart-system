package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;
import com.aliyara.inventoryservice.application.mapper.ProductDtoMapper;
import com.aliyara.inventoryservice.application.port.in.ProductUseCase;
import com.aliyara.inventoryservice.domain.exception.DuplicateSkuException;
import com.aliyara.inventoryservice.domain.exception.InsufficientStockException;
import com.aliyara.inventoryservice.domain.exception.ProductNotFoundException;
import com.aliyara.inventoryservice.domain.port.ProductRepository;
import com.aliyara.inventoryservice.domain.port.StockRepository;
import com.aliyara.inventoryservice.domain.product.Product;
import com.aliyara.inventoryservice.domain.stock.Stock;
import com.aliyara.inventoryservice.domain.stock.enums.StockStatus;
import com.aliyara.inventoryservice.infrastructure.config.TenantContextHolder;
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
    private final StockRepository stockRepository;
    private final ProductDtoMapper productDtoMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        String tenantId = TenantContextHolder.getTenantId();
        if(request.getStock() <= 0) {
            throw new IllegalArgumentException("Product stock cannot be less than 1!");
        }

        if (request.getStock() > request.getMaxStock()) {
            throw new IllegalArgumentException("Product stock cannot be higher than max stock!!");
        }

        if (productRepository.existsBySku(request.getSku(), tenantId)) {
            throw new DuplicateSkuException(
                    "Product with SKU '" + request.getSku() + "' already exists for this tenant");
        }
        Product product = productDtoMapper.toDomain(request, tenantId);
        Product saved = productRepository.save(product);
        Stock stock = new  Stock.Builder()
                .productId(saved.getId())
                .minStock(request.getMinStock())
                .maxStock(request.getMaxStock())
                .quantityTotal(request.getStock())
                .status(request.getStock() <= request.getMinStock() ? StockStatus.LOW_STOCK : StockStatus.IN_STOCK)
                .build();
        stockRepository.save(stock);
        return productDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {
        String tenantId = TenantContextHolder.getTenantId();
        Product product = productRepository.findAllByTenantId(tenantId)
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productDtoMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        String tenantId = TenantContextHolder.getTenantId();
        return productRepository.findAllByTenantId(tenantId)
                .stream()
                .map(productDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        String tenantId = TenantContextHolder.getTenantId();
        Product existing = productRepository.findAllByTenantId(tenantId)
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        existing.updateDetails(request.getName(), request.getDescription(), request.getPrice());
        existing.updateSku(request.getSku());
        Product saved = productRepository.save(existing);
        return productDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        String tenantId = TenantContextHolder.getTenantId();
        productRepository.findAllByTenantId(tenantId)
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepository.deleteById(id);
    }
}
