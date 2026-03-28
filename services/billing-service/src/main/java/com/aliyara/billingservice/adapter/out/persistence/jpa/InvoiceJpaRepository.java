package com.aliyara.billingservice.adapter.out.persistence.jpa;

import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, UUID> {

    List<InvoiceEntity> findAllByTenantId(String tenantId);

    boolean existsByQuoteIdAndTenantId(UUID quoteId, String tenantId);
}
