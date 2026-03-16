package com.aliyara.companyservice.interfaces.dto.request;

import com.aliyara.companyservice.domain.enums.SubscriptionPlan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCompanyRequest {

    @Valid
    @NotNull(message = "Company details are required")
    private CompanyDto company;

    @Valid
    @NotNull(message = "Owner details are required")
    private OwnerDto owner;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyDto {
        @NotBlank(message = "Company name is required")
        private String name;

        @NotBlank(message = "Company email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Phone is required")
        private String phone;

        @NotBlank(message = "Address is required")
        private String address;

        @NotBlank(message = "Industry is required")
        private String industry;

        @NotNull(message = "Subscription plan is required")
        private SubscriptionPlan subscriptionPlan;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OwnerDto {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;
    }
}
