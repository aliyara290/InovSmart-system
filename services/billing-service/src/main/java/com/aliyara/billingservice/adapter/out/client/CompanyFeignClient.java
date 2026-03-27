package com.aliyara.billingservice.adapter.out.client;

import com.aliyara.billingservice.adapter.out.client.dto.CompanyClientResponse;
import com.aliyara.billingservice.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "company-service",
        configuration = FeignConfig.class
)
public interface CompanyFeignClient {

    @GetMapping("/api/v1/companies/{tenantId}")
    CompanyClientResponse getCompany(@PathVariable("tenantId") String tenantId);
}
