package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.category.CategoryRequest;
import com.aliyara.inventoryservice.application.dto.category.CategoryResponse;
import com.aliyara.inventoryservice.application.mapper.CategoryDtoMapper;
import com.aliyara.inventoryservice.application.port.in.CategoryUseCase;
import com.aliyara.inventoryservice.domain.category.Category;
import com.aliyara.inventoryservice.domain.exception.CategoryNotFoundException;
import com.aliyara.inventoryservice.domain.port.CategoryRepository;
import com.aliyara.inventoryservice.infrastructure.config.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        String tenantId = TenantContextHolder.getTenantId();
        Category category = categoryDtoMapper.toDomain(request, tenantId);
        Category saved = categoryRepository.save(category);
        return categoryDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(UUID id) {
        String tenantId = TenantContextHolder.getTenantId();
        Category category = categoryRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return categoryDtoMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        String tenantId = TenantContextHolder.getTenantId();
        return categoryRepository.findAllByTenantId(tenantId)
                .stream()
                .map(categoryDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        String tenantId = TenantContextHolder.getTenantId();
        categoryRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        String tenantId = TenantContextHolder.getTenantId();
        Category category = categoryRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        category.updateName(request.getName());
        category.updateDescription(request.getDescription());
        Category saved = categoryRepository.save(category);
        return categoryDtoMapper.toResponse(saved);
    }
}
