package com.aliyara.companyservice.application.port.out;

import com.aliyara.companyservice.domain.model.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepositoryPort {
    Company save(Company company);
    Optional<Company> findByTenantId(UUID tenantId);
    void delete(Company company);
    boolean existsByTenantId(UUID tenantId);
}
