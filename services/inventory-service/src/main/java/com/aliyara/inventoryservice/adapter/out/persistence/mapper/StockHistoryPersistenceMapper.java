package com.aliyara.inventoryservice.adapter.out.persistence.mapper;

import com.aliyara.inventoryservice.adapter.out.persistence.entity.StockHistoryEntity;
import com.aliyara.inventoryservice.domain.model.stock.StockHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockHistoryPersistenceMapper {

    StockHistoryEntity toEntity(StockHistory stockHistory);

    default StockHistory toDomain(StockHistoryEntity entity) {
        if (entity == null)
            return null;
        return new StockHistory.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .productId(entity.getProductId())
                .movementType(entity.getMovementType())
                .quantityChange(entity.getQuantityChange())
                .quantityBefore(entity.getQuantityBefore())
                .quantityAfter(entity.getQuantityAfter())
                .reason(entity.getReason())
                .referenceType(entity.getReferenceType())
                .referenceId(entity.getReferenceId())
                .performedBy(entity.getPerformedBy())
                .build();
    }
}
