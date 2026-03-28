package com.aliyara.supplyservice.application.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierRequest {

    @NotBlank(message = "Supplier name is required")
    private String name;

    @Email(message = "Contact email must be valid")
    private String contactEmail;

    private String phone;

    private String address;
}
