package com.aliyara.inventoryservice.adapter.out.persistence.mapper;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.StockEntity;
import com.aliyara.inventoryservice.domain.model.stock.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockPersistenceMapper {

    StockEntity toEntity(Stock stock);

    default Stock toDomain(StockEntity entity) {
        if (entity == null)
            return null;
        return new Stock.Builder()
                .id(entity.getId())
                .quantityTotal(entity.getQuantityTotal())
                .quantityReserved(entity.getQuantityReserved())
                .status(entity.getStatus())
                .minStock(entity.getMinStock())
                .lastUpdatedAt(entity.getLastUpdatedAt())
                .productId(entity.getProductId())
                .build();
    }
}
