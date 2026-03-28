package com.aliyara.supplyservice.application.port.out;

import com.aliyara.supplyservice.domain.model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findAllByTenantId(String tenantId);
    List<Order> findAllByTenantIdAndSupplierId(String tenantId, UUID supplierId);
    String generateOrderNumber(String tenantId);
}
