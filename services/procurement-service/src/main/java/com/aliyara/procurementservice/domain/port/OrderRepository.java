package com.aliyara.procurementservice.domain.port;

import com.aliyara.procurementservice.domain.models.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAllByTenantId(String tenantId);

    void deleteById(UUID id);
}
