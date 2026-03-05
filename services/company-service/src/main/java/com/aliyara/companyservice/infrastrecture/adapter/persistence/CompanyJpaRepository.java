package com.aliyara.companyservice.infrastrecture.adapter.persistence;

import com.aliyara.companyservice.infrastrecture.adapter.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByTenantId(UUID tenantId);
}
