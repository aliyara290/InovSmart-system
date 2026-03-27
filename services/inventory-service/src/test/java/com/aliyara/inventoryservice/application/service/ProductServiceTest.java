package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;
import com.aliyara.inventoryservice.application.mapper.ProductDtoMapper;
import com.aliyara.inventoryservice.domain.exception.DuplicateSkuException;
import com.aliyara.inventoryservice.domain.exception.ProductNotFoundException;
import com.aliyara.inventoryservice.domain.model.product.Product;
import com.aliyara.inventoryservice.domain.model.stock.Stock;
import com.aliyara.inventoryservice.domain.port.ProductRepository;
import com.aliyara.inventoryservice.domain.port.StockRepository;
import com.aliyara.inventoryservice.infrastructure.config.TenantContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductDtoMapper productDtoMapper;

    @InjectMocks
    private ProductService productService;

    private static final String TENANT_ID = "tenant-123";
    private UUID productId;
    private Product product;
    private ProductRequest request;
    private ProductResponse response;

    @BeforeEach
    void setUp() {
        TenantContextHolder.setTenantId(TENANT_ID);
        productId = UUID.randomUUID();
        
        product = new Product.Builder()
                .id(productId)
                .name("Laptop")
                .description("Gaming laptop")
                .sku("SKU-123")
                .price(1500.00)
                .tenantId(TENANT_ID)
                .build();
                
        request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("Gaming laptop");
        request.setSku("SKU-123");
        request.setPrice(1500.00);
        request.setStock(50);
        request.setMinStock(10);
        request.setMaxStock(100);
        
        response = new ProductResponse();
        response.setId(productId);
        response.setName("Laptop");
        response.setDescription("Gaming laptop");
        response.setSku("SKU-123");
        response.setPrice(1500.00);
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    void createProduct_shouldCreateProduct_whenValidRequest() {
        when(productRepository.existsBySku(request.getSku(), TENANT_ID)).thenReturn(false);
        when(productDtoMapper.toDomain(request, TENANT_ID)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(stockRepository.save(any(Stock.class))).thenReturn(any());
        when(productDtoMapper.toResponse(product)).thenReturn(response);

        ProductResponse result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        verify(productRepository).save(product);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void createProduct_shouldThrowException_whenStockIsZero() {
        request.setStock(0);

        assertThrows(IllegalArgumentException.class, 
                () -> productService.createProduct(request));
    }

    @Test
    void createProduct_shouldThrowException_whenStockExceedsMaxStock() {
        request.setStock(150);
        request.setMaxStock(100);

        assertThrows(IllegalArgumentException.class, 
                () -> productService.createProduct(request));
    }

    @Test
    void createProduct_shouldThrowException_whenSkuExists() {
        when(productRepository.existsBySku(request.getSku(), TENANT_ID)).thenReturn(true);

        assertThrows(DuplicateSkuException.class, 
                () -> productService.createProduct(request));
    }

    @Test
    void getProduct_shouldReturnProduct_whenExists() {
        when(productRepository.findAllByTenantId(TENANT_ID))
                .thenReturn(List.of(product));
        when(productDtoMapper.toResponse(product)).thenReturn(response);

        ProductResponse result = productService.getProduct(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
    }

    @Test
    void getProduct_shouldThrowException_whenNotFound() {
        when(productRepository.findAllByTenantId(TENANT_ID))
                .thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, 
                () -> productService.getProduct(productId));
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        List<Product> products = List.of(product);
        when(productRepository.findAllByTenantId(TENANT_ID)).thenReturn(products);
        when(productDtoMapper.toResponse(product)).thenReturn(response);

        List<ProductResponse> results = productService.getAllProducts();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(productRepository).findAllByTenantId(TENANT_ID);
    }

    @Test
    void updateProduct_shouldUpdateProduct_whenExists() {
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Laptop");
        updateRequest.setDescription("Updated description");
        updateRequest.setSku("SKU-123");
        updateRequest.setPrice(1500.00);

        when(productRepository.findAllByTenantId(TENANT_ID))
                .thenReturn(List.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productDtoMapper.toResponse(product)).thenReturn(response);

        ProductResponse result = productService.updateProduct(productId, updateRequest);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldThrowException_whenNotFound() {
        when(productRepository.findAllByTenantId(TENANT_ID))
                .thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, 
                () -> productService.updateProduct(productId, request));
    }

    @Test
    void deleteProduct_shouldDeleteProduct_whenExists() {
        when(productRepository.findAllByTenantId(TENANT_ID))
                .thenReturn(List.of(product));

        productService.deleteProduct(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProduct_shouldThrowException_whenNotFound() {
        when(productRepository.findAllByTenantId(TENANT_ID))
                .thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, 
                () -> productService.deleteProduct(productId));
    }
}
