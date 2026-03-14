package com.aliyara.billingservice.domain.port;

import com.aliyara.billingservice.domain.model.invoice.Invoice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);

    Optional<Invoice> findById(UUID id);

    List<Invoice> findAllByTenantId(String tenantId);
}
