package com.aliyara.procurementservice.application.dto.supplier;

import com.aliyara.procurementservice.domain.models.supplier.enums.SupplierStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SupplierResponse {
    private UUID id;
    private String tenantId;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;
    private SupplierStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
