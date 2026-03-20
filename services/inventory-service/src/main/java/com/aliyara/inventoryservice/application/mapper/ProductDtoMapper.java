package com.aliyara.inventoryservice.application.mapper;

import com.aliyara.inventoryservice.application.dto.product.ProductRequest;
import com.aliyara.inventoryservice.application.dto.product.ProductResponse;
import com.aliyara.inventoryservice.domain.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    default Product toDomain(ProductRequest request, String tenantId) {
        if (request == null)
            return null;
        return new Product.Builder()
                .tenantId(tenantId)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .sku(request.getSku())
                .categoryId(request.getCategoryId())
                .build();
    }

//    @Mapping(target = "stock", ignore = true)
//    @Mapping(target = "minStock", ignore = true)
//    @Mapping(target = "maxStock", ignore = true)
    ProductResponse toResponse(Product product);
}
