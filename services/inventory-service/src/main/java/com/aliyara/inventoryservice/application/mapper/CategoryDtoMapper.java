package com.aliyara.inventoryservice.application.mapper;

import com.aliyara.inventoryservice.application.dto.category.CategoryRequest;
import com.aliyara.inventoryservice.application.dto.category.CategoryResponse;
import com.aliyara.inventoryservice.domain.category.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {

    default Category toDomain(CategoryRequest request, String tenantId) {
        if (request == null)
            return null;
        return new Category.Builder()
                .tenantId(tenantId)
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    CategoryResponse toResponse(Category category);
}
