package com.aliyara.inventoryservice.application.mapper;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;
import com.aliyara.inventoryservice.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    default Product toDomain(ProductRequest request) {
        if (request == null)
            return null;
        return new Product.Builder()
                .tenantId(request.getTenantId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .sku(request.getSku())
                .categoryId(request.getCategoryId())
                .build();
    }

    ProductResponse toResponse(Product product);
}
