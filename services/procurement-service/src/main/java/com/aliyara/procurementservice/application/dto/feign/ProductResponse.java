package com.aliyara.procurementservice.application.dto.feign;

import lombok.Data;

import java.util.UUID;

/**
 * Minimal projection of a Product as returned by the Inventory Service.
 * Only the fields required for validation are mapped.
 */
@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private boolean active;
}
