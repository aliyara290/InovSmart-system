package com.aliyara.billingservice.adapter.out.client.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustStockClientRequest {
    private int newQuantityTotal;
    private String reason;
    private String referenceType;
    private UUID referenceId;
    private UUID performedBy;
}
