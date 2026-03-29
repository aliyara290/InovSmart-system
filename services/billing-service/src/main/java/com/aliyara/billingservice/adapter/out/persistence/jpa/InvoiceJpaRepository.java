package com.aliyara.billingservice.adapter.out.persistence.jpa;

import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceEntity;
import com.aliyara.billingservice.domain.model.invoice.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, UUID> {

    List<InvoiceEntity> findAllByTenantId(String tenantId);

    boolean existsByQuoteIdAndTenantId(UUID quoteId, String tenantId);
    List<InvoiceEntity> findAllByStatusAndTenantId(InvoiceStatus status, String tenantId);
}
