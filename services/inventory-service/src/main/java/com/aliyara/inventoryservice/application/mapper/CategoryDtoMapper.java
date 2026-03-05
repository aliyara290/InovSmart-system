package com.aliyara.inventoryservice.application.mapper;

import com.aliyara.inventoryservice.application.dto.category.CategoryRequest;
import com.aliyara.inventoryservice.application.dto.category.CategoryResponse;
import com.aliyara.inventoryservice.domain.category.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {

    default Category toDomain(CategoryRequest request) {
        if (request == null)
            return null;
        return new Category.Builder()
                .name(request.getName())
                .build();
    }

    CategoryResponse toResponse(Category category);
}
