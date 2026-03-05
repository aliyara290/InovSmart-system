package com.aliyara.companyservice.domain.exception;

import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(CompanyId companyId) {
        super("Company not found with id: " + companyId.id());
    }

    public CompanyNotFoundException(TenantId tenantId) {
        super("Company not found for tenant: " + tenantId.id());
    }

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
