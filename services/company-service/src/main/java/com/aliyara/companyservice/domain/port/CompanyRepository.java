package com.aliyara.companyservice.domain.port;

import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;

import java.util.Optional;

public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> existById(CompanyId id);
    Optional<Company> findById(CompanyId id);
    Optional<Company> findByTenantId(TenantId tenantId);
    void deleteById(CompanyId companyId);
}