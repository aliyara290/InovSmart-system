package com.aliyara.inventoryservice.adapter.out.persistence.mapper;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.CategoryEntity;
import com.aliyara.inventoryservice.domain.category.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {

    CategoryEntity toEntity(Category category);

    default Category toDomain(CategoryEntity entity) {
        if (entity == null)
            return null;
        return new Category.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .build();
    }
}
