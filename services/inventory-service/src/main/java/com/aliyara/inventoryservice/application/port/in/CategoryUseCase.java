package com.aliyara.inventoryservice.application.port.in;

import com.aliyara.inventoryservice.application.dto.category.CategoryRequest;
import com.aliyara.inventoryservice.application.dto.category.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryUseCase {
    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategory(UUID id);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(UUID id);
}
