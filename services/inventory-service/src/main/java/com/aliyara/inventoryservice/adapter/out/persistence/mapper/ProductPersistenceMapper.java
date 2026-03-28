package com.aliyara.inventoryservice.adapter.out.persistence.mapper;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.ProductEntity;
import com.aliyara.inventoryservice.domain.model.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    ProductEntity toEntity(Product product);

    default Product toDomain(ProductEntity entity) {
        if (entity == null)
            return null;
        return new Product.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .sku(entity.getSku())
                .productId(entity.getProductId())
                .categoryId(entity.getCategoryId())
                .build();
    }
}
