package com.aliyara.companyservice.application.port.out;

import com.aliyara.companyservice.domain.model.CompanyInvitation;

import java.util.Optional;
import java.util.UUID;

public interface CompanyInvitationRepositoryPort {
    CompanyInvitation save(CompanyInvitation invitation);
    Optional<CompanyInvitation> findByToken(String token);
    Optional<CompanyInvitation> findByTenantIdAndEmail(UUID tenantId, String email);
}
