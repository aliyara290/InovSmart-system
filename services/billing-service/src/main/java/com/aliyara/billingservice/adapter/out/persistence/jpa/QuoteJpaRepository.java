package com.aliyara.billingservice.adapter.out.persistence.jpa;

import com.aliyara.billingservice.adapter.out.persistence.entity.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuoteJpaRepository extends JpaRepository<QuoteEntity, UUID> {
    List<QuoteEntity> findAllByTenantId(String tenantId);
}
