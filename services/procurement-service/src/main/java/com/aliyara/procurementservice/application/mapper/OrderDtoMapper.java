package com.aliyara.procurementservice.application.mapper;

import com.aliyara.procurementservice.application.dto.order.CreateOrderItemRequest;
import com.aliyara.procurementservice.application.dto.order.OrderItemResponse;
import com.aliyara.procurementservice.application.dto.order.OrderResponse;
import com.aliyara.procurementservice.domain.models.order.Order;
import com.aliyara.procurementservice.domain.models.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

    @Mapping(target = "subtotal", expression = "java(item.subtotal())")
    OrderItemResponse toItemResponse(OrderItem item);

    List<OrderItemResponse> toItemResponseList(List<OrderItem> items);

    @Mapping(target = "items", expression = "java(toItemResponseList(order.getItems()))")
    OrderResponse toResponse(Order order);

    default List<OrderItem> toOrderItemList(List<CreateOrderItemRequest> requests) {
        if (requests == null)
            return List.of();
        return requests.stream()
                .map(r -> new OrderItem.Builder()
                        .productId(r.getProductId())
                        .quantity(r.getQuantity())
                        .unitPrice(r.getUnitPrice())
                        .build())
                .toList();
    }
}
