package com.aliyara.companyservice.application.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyResponse {
    private UUID id;
    private UUID tenantId;
    private String name;
    private String email;
    private String phone;
    private String website;
    private int size;
    private int foundedYear;
    private String description;
    private CompanyRequest.AddressDto address;
    private CompanyRequest.CompanySettingsDto companySettings;
}
