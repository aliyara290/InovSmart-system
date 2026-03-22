package com.aliyara.billingservice.adapter.out.client.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveStockClientRequest {
    private int quantity;
    private String reason;
    private String referenceType;
    private UUID referenceId;
    private UUID performedBy;
}
