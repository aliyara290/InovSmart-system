package com.aliyara.companyservice.domain.exception;

import java.util.UUID;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(UUID tenantId) {
        super("Company not found for tenantId: " + tenantId);
    }
}
