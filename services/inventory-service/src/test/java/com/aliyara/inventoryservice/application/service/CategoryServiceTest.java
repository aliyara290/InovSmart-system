package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.category.CategoryRequest;
import com.aliyara.inventoryservice.application.dto.category.CategoryResponse;
import com.aliyara.inventoryservice.application.mapper.CategoryDtoMapper;
import com.aliyara.inventoryservice.domain.exception.CategoryNotFoundException;
import com.aliyara.inventoryservice.domain.model.category.Category;
import com.aliyara.inventoryservice.domain.port.CategoryRepository;
import com.aliyara.inventoryservice.infrastructure.config.TenantContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryDtoMapper categoryDtoMapper;

    @InjectMocks
    private CategoryService categoryService;

    private static final String TENANT_ID = "tenant-123";
    private UUID categoryId;
    private Category category;
    private CategoryRequest request;
    private CategoryResponse response;

    @BeforeEach
    void setUp() {
        TenantContextHolder.setTenantId(TENANT_ID);
        categoryId = UUID.randomUUID();
        
        category = new Category.Builder()
                .id(categoryId)
                .name("Electronics")
                .description("Electronic items")
                .tenantId(TENANT_ID)
                .build();
                
        request = new CategoryRequest();
        request.setName("Electronics");
        request.setDescription("Electronic items");
        
        response = new CategoryResponse();
        response.setId(categoryId);
        response.setName("Electronics");
//        response.setDescription("Electronic items");
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    void createCategory_shouldCreateAndReturnCategory() {
        when(categoryDtoMapper.toDomain(request, TENANT_ID)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryDtoMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.createCategory(request);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void getCategory_shouldReturnCategory_whenExists() {
        when(categoryRepository.findByIdAndTenantId(categoryId, TENANT_ID))
                .thenReturn(Optional.of(category));
        when(categoryDtoMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.getCategory(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        verify(categoryRepository).findByIdAndTenantId(categoryId, TENANT_ID);
    }

    @Test
    void getCategory_shouldThrowException_whenNotFound() {
        when(categoryRepository.findByIdAndTenantId(categoryId, TENANT_ID))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, 
                () -> categoryService.getCategory(categoryId));
    }

    @Test
    void getAllCategories_shouldReturnAllCategories() {
        List<Category> categories = List.of(category);
        when(categoryRepository.findAllByTenantId(TENANT_ID)).thenReturn(categories);
        when(categoryDtoMapper.toResponse(category)).thenReturn(response);

        List<CategoryResponse> results = categoryService.getAllCategories();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(categoryRepository).findAllByTenantId(TENANT_ID);
    }

    @Test
    void updateCategory_shouldUpdateAndReturnCategory() {
        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setDescription("Updated Description");

        when(categoryRepository.findByIdAndTenantId(categoryId, TENANT_ID))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryDtoMapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = categoryService.updateCategory(categoryId, updateRequest);

        assertNotNull(result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_shouldThrowException_whenNotFound() {
        when(categoryRepository.findByIdAndTenantId(categoryId, TENANT_ID))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, 
                () -> categoryService.updateCategory(categoryId, request));
    }

    @Test
    void deleteCategory_shouldDeleteCategory_whenExists() {
        when(categoryRepository.findByIdAndTenantId(categoryId, TENANT_ID))
                .thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void deleteCategory_shouldThrowException_whenNotFound() {
        when(categoryRepository.findByIdAndTenantId(categoryId, TENANT_ID))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, 
                () -> categoryService.deleteCategory(categoryId));
    }
}
