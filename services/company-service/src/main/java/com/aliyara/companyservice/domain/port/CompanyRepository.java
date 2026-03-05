package com.aliyara.companyservice.domain.port;

import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {

    Company save(Company company);

    Optional<Company> findById(CompanyId id);

    Optional<Company> findByTenantId(TenantId tenantId);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByName(String name);

    List<Company> findAll();

    boolean existsByTenantId(TenantId tenantId);

    boolean existsByName(String name);

    void deleteById(CompanyId id);
}