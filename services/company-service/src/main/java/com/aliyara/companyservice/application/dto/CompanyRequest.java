package com.aliyara.companyservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CompanyRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    private String website;
    private int size;
    private int foundedYear;
    private String description;

    private AddressDto address;
    private CompanySettingsDto companySettings;

    @Data
    public static class AddressDto {
        @NotBlank
        private String streetLine1;
        private String streetLine2;
        @NotBlank
        private String city;
        private String stateProvince;
        private String postalCode;
        @NotBlank
        private String country;
    }

    @Data
    public static class CompanySettingsDto {
        private String logo;
        private Integer vat;
        private String currency;
        private String language;
    }
}
