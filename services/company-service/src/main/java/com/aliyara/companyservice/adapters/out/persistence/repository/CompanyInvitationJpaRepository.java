package com.aliyara.companyservice.adapters.out.persistence.repository;

import com.aliyara.companyservice.adapters.out.persistence.entity.CompanyInvitationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyInvitationJpaRepository extends JpaRepository<CompanyInvitationJpaEntity, UUID> {
    Optional<CompanyInvitationJpaEntity> findByToken(String token);
    Optional<CompanyInvitationJpaEntity> findByTenantIdAndEmail(UUID tenantId, String email);
}
