package com.aliyara.procurementservice.adapter.out.persistence.mapper;

import com.aliyara.procurementservice.adapter.out.persistence.entity.OrderEntity;
import com.aliyara.procurementservice.adapter.out.persistence.entity.OrderItemEntity;
import com.aliyara.procurementservice.domain.models.order.Order;
import com.aliyara.procurementservice.domain.models.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {

    // ── Entity → Domain ───────────────────────────────────────────────────────

    default OrderItem toItemDomain(OrderItemEntity entity) {
        if (entity == null)
            return null;
        return new OrderItem.Builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .deliveredQuantity(entity.getDeliveredQuantity())
                .build();
    }

    default Order toDomain(OrderEntity entity) {
        if (entity == null)
            return null;
        List<OrderItem> items = entity.getItems() == null ? List.of()
                : entity.getItems().stream().map(this::toItemDomain).toList();
        return new Order.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .supplierId(entity.getSupplierId())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .items(items)
                .totalTTC(entity.getTotalTTC())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // ── Domain → Entity ───────────────────────────────────────────────────────

    default OrderItemEntity toItemEntity(OrderItem item, OrderEntity parentOrder) {
        if (item == null)
            return null;
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(item.getId());
        entity.setProductId(item.getProductId());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setDeliveredQuantity(item.getDeliveredQuantity());
        entity.setOrder(parentOrder);
        return entity;
    }

    default OrderEntity toEntity(Order order) {
        if (order == null)
            return null;
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setTenantId(order.getTenantId());
        entity.setSupplierId(order.getSupplierId());
        entity.setStatus(order.getStatus());
        entity.setNotes(order.getNotes());
        entity.setTotalExcludingTax(order.getTotalExcludingTax());
        entity.setTotalTTC(order.getTotalTTC());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .toList();
        entity.setItems(itemEntities);
        return entity;
    }
}
