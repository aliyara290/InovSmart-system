package com.aliyara.inventoryservice.application.dto.stock;

import com.aliyara.inventoryservice.domain.stock.enums.MovementType;
import com.aliyara.inventoryservice.domain.stock.enums.ReferenceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StockHistoryResponse {
    private UUID id;
    private String tenantId;
    private UUID productId;
    private MovementType movementType;
    private int quantityChange;
    private int quantityBefore;
    private int quantityAfter;
    private String reason;
    private ReferenceType referenceType;
    private UUID referenceId;
    private UUID performedBy;
}
