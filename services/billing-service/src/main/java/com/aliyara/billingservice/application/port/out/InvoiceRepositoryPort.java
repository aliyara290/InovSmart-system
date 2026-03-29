package com.aliyara.billingservice.application.port.out;

import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.enums.InvoiceStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepositoryPort {

    Invoice save(Invoice invoice);

    Optional<Invoice> findById(UUID id);

    List<Invoice> findAllByTenantId(String tenantId);

    boolean existsByQuoteIdAndTenantId(UUID quoteId, String tenantId);


    List<Invoice> findAllByStatusAndTenantId(InvoiceStatus status,  String tenantId);
}
