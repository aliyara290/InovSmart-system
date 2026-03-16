package com.aliyara.companyservice.application.port.out;

import com.aliyara.companyservice.domain.model.CompanyUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyUserRepositoryPort {
    CompanyUser save(CompanyUser companyUser);
    Optional<CompanyUser> findById(UUID id);
    Optional<CompanyUser> findByTenantIdAndUserId(UUID tenantId, UUID userId);
    Optional<CompanyUser> findByTenantIdAndEmail(UUID tenantId, String email);
    List<CompanyUser> findByTenantId(UUID tenantId);
    void delete(CompanyUser companyUser);
    boolean existsByTenantIdAndEmail(UUID tenantId, String email);
}
