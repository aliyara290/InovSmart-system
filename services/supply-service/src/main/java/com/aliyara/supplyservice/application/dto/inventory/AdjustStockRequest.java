package com.aliyara.supplyservice.application.dto.inventory;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustStockRequest {
    private int newQuantityTotal;
    private String reason;
    private String referenceType;
    private UUID referenceId;
    private UUID performedBy;
}
