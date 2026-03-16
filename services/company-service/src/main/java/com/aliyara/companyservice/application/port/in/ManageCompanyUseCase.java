package com.aliyara.companyservice.application.port.in;

import com.aliyara.companyservice.domain.enums.CompanyStatus;
import com.aliyara.companyservice.interfaces.dto.request.UpdateCompanyRequest;
import com.aliyara.companyservice.interfaces.dto.response.CompanyResponse;

import java.util.UUID;

public interface ManageCompanyUseCase {
    CompanyResponse getByTenantId(UUID tenantId);
    CompanyResponse update(UUID tenantId, UpdateCompanyRequest request);
    void updateStatus(UUID tenantId, CompanyStatus status);
    void delete(UUID tenantId);
}
