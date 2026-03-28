package com.aliyara.supplyservice.adapters.out.persistence.mapper;

import com.aliyara.supplyservice.adapters.out.persistence.entity.OrderEntity;
import com.aliyara.supplyservice.adapters.out.persistence.entity.OrderItemEntity;
import com.aliyara.supplyservice.domain.model.order.Order;
import com.aliyara.supplyservice.domain.model.order.OrderItem;
import com.aliyara.supplyservice.domain.model.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceMapper {

    public Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(this::toItemDomain)
                .collect(Collectors.toList());

        return new Order.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .supplierId(entity.getSupplierId())
                .orderNumber(entity.getOrderNumber())
                .status(OrderStatus.valueOf(entity.getStatus()))
                .notes(entity.getNotes())
                .items(items)
                .totalTTC(entity.getTotalTTC())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public OrderEntity toEntity(Order domain) {
        OrderEntity entity = OrderEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .supplierId(domain.getSupplierId())
                .orderNumber(domain.getOrderNumber())
                .status(domain.getStatus().name())
                .notes(domain.getNotes())
                .totalExcludingTax(domain.getTotalExcludingTax())
                .totalTTC(domain.getTotalTTC())
                .createdBy(domain.getCreatedBy())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();

        List<OrderItemEntity> itemEntities = domain.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .collect(Collectors.toList());

        entity.setItems(itemEntities);
        return entity;
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {
        return new OrderItem.Builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .deliveredQuantity(entity.getDeliveredQuantity())
                .build();
    }

    private OrderItemEntity toItemEntity(OrderItem domain, OrderEntity orderEntity) {
        return OrderItemEntity.builder()
                .id(domain.getId())
                .productId(domain.getProductId())
                .quantity(domain.getQuantity())
                .unitPrice(domain.getUnitPrice())
                .deliveredQuantity(domain.getDeliveredQuantity())
                .order(orderEntity)
                .build();
    }
}
