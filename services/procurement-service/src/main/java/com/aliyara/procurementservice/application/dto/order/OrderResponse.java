package com.aliyara.procurementservice.application.dto.order;

import com.aliyara.procurementservice.domain.models.order.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private String tenantId;
    private UUID supplierId;
    private OrderStatus status;
    private String notes;
    private BigDecimal totalExcludingTax;
    private BigDecimal totalTTC;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
