package com.aliyara.inventoryservice.application.dto.stock;

import com.aliyara.inventoryservice.domain.stock.enums.ReferenceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdjustStockRequest {

    @Positive(message = "newQuantityTotal must be positive")
    private int newQuantityTotal;

    private String reason;

    @NotNull(message = "referenceType is required")
    private ReferenceType referenceType;

    private UUID referenceId;

    @NotNull(message = "performedBy is required")
    private UUID performedBy;
}
