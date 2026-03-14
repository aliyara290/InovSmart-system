package com.aliyara.procurementservice.application.port.in;

import com.aliyara.procurementservice.application.dto.order.CreateOrderRequest;
import com.aliyara.procurementservice.application.dto.order.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderUseCase {
    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrder(UUID id);

    List<OrderResponse> getAllOrders(String tenantId);

    OrderResponse markAsSent(UUID id);

    OrderResponse markAsDelivered(UUID id);

    OrderResponse cancelOrder(UUID id);
}
