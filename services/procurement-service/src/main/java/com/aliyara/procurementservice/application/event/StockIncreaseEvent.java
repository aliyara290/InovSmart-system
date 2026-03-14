package com.aliyara.procurementservice.application.event;

import java.util.UUID;

public record StockIncreaseEvent(
        UUID orderId,
        UUID productId,
        int quantity) {
}
