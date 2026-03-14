package com.aliyara.procurementservice.application.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemResponse {
    private UUID id;
    private UUID productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private int deliveredQuantity;
}
