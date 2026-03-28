package com.aliyara.companyservice.interfaces.dto.response;

import com.aliyara.companyservice.domain.enums.CompanyStatus;
import com.aliyara.companyservice.domain.enums.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private UUID id;
    private UUID tenantId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String industry;
    private SubscriptionPlan subscriptionPlan;
    private CompanyStatus status;
    private UUID ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
