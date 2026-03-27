package com.aliyara.billingservice.adapter.out.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyClientResponse {

    private String id;
    private String tenantId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String industry;
    private String subscriptionPlan;
    private String status;
    private String ownerId;
    private String createdAt;
    private String updatedAt;
}
