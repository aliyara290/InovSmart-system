package com.aliyara.procurementservice.domain.port;

import java.util.UUID;

/**
 * Idempotency guard for order delivery events.
 * Prevents the same order from being marked as delivered more than once,
 * and prevents duplicate stock-increase events being published to RabbitMQ.
 */
public interface DeliveredOrderRepository {
    boolean existsByOrderId(UUID orderId);

    void save(UUID orderId);
}
