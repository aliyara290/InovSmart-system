package com.aliyara.billingservice.adapter.out.client;

import com.aliyara.billingservice.adapter.out.client.dto.CompanyClientResponse;
import com.aliyara.billingservice.application.port.out.CompanyServicePort;
import com.aliyara.billingservice.domain.exception.DomainException;
import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyServiceAdapter implements CompanyServicePort {

    private static final String COMPANY_SERVICE = "companyService";

    private final CompanyFeignClient companyFeignClient;

    @Override
    @CircuitBreaker(name = COMPANY_SERVICE)
    @Retry(name = COMPANY_SERVICE)
    public CompanySnapshot getCompanySnapshot(String tenantId) {
        try {
            CompanyClientResponse response = companyFeignClient.getCompany(tenantId);
            return new CompanySnapshot(
                    response.getName(),
                    response.getEmail(),
                    response.getPhone(),
                    response.getAddress()
            );
        } catch (Exception e) {
            log.error("Failed to fetch company data for tenantId: {}", tenantId, e);
            throw new DomainException("Unable to fetch company data for tenant: " + tenantId, e);
        }
    }
}
