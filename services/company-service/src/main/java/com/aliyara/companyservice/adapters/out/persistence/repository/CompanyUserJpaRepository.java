package com.aliyara.companyservice.adapters.out.persistence.repository;

import com.aliyara.companyservice.adapters.out.persistence.entity.CompanyUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyUserJpaRepository extends JpaRepository<CompanyUserJpaEntity, UUID> {
    Optional<CompanyUserJpaEntity> findByTenantIdAndUserId(UUID tenantId, UUID userId);
    Optional<CompanyUserJpaEntity> findByTenantIdAndEmail(UUID tenantId, String email);
    List<CompanyUserJpaEntity> findByTenantId(UUID tenantId);
    boolean existsByTenantIdAndEmail(UUID tenantId, String email);
}
