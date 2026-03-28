package com.aliyara.companyservice.adapters.out.persistence.repository;

import com.aliyara.companyservice.adapters.out.persistence.entity.CompanyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyJpaEntity, UUID> {
    Optional<CompanyJpaEntity> findByTenantId(UUID tenantId);
    boolean existsByTenantId(UUID tenantId);
}
