package com.aliyara.procurementservice.application.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSupplierRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotBlank(message = "Supplier name is required")
    private String name;

    @Email(message = "Contact email must be valid")
    private String contactEmail;

    private String phone;
    private String address;
}
