package com.aliyara.inventoryservice.application.service;

import com.aliyara.inventoryservice.application.dto.category.CategoryRequest;
import com.aliyara.inventoryservice.application.dto.category.CategoryResponse;
import com.aliyara.inventoryservice.application.mapper.CategoryDtoMapper;
import com.aliyara.inventoryservice.application.port.in.CategoryUseCase;
import com.aliyara.inventoryservice.domain.category.Category;
import com.aliyara.inventoryservice.domain.exception.CategoryNotFoundException;
import com.aliyara.inventoryservice.domain.port.CategoryRepository;
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
        Category category = categoryDtoMapper.toDomain(request);
        Category saved = categoryRepository.save(category);
        return categoryDtoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return categoryDtoMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        categoryRepository.deleteById(id);
    }
}
