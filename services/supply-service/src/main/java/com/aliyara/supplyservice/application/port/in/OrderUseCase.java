package com.aliyara.supplyservice.application.port.in;

import com.aliyara.supplyservice.application.dto.order.CreateOrderRequest;
import com.aliyara.supplyservice.application.dto.order.OrderResponse;
import com.aliyara.supplyservice.application.dto.order.UpdateOrderStatusRequest;
import com.aliyara.supplyservice.application.dto.inventory.LowStockProductResponse;

import java.util.List;
import java.util.UUID;

public interface OrderUseCase {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrder(UUID id);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersBySupplier(UUID supplierId);
    OrderResponse updateOrderStatus(UUID id, UpdateOrderStatusRequest request);
    void cancelOrder(UUID id);
    List<LowStockProductResponse> getLowStockProducts();
}
