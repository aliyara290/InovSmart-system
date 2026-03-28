package com.aliyara.supplyservice.application.dto.order;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private UUID id;
    private String orderNumber;
    private UUID supplierId;
    private String supplierName;
    private String status;
    private String notes;
    private BigDecimal totalExcludingTax;
    private BigDecimal totalTTC;
    private String createdBy;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
