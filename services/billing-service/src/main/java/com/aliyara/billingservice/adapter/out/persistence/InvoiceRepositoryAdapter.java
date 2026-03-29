package com.aliyara.billingservice.adapter.out.persistence;

import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceEntity;
import com.aliyara.billingservice.adapter.out.persistence.jpa.InvoiceJpaRepository;
import com.aliyara.billingservice.adapter.out.persistence.mapper.InvoicePersistenceMapper;
import com.aliyara.billingservice.application.port.out.InvoiceRepositoryPort;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.enums.InvoiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class InvoiceRepositoryAdapter implements InvoiceRepositoryPort {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final InvoicePersistenceMapper invoicePersistenceMapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = invoicePersistenceMapper.toEntityWithLines(invoice);
        InvoiceEntity saved = invoiceJpaRepository.save(entity);
        return invoicePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Invoice> findById(UUID id) {
        return invoiceJpaRepository.findById(id)
                .map(invoicePersistenceMapper::toDomain);
    }

    @Override
    public List<Invoice> findAllByTenantId(String tenantId) {
        return invoiceJpaRepository.findAllByTenantId(tenantId).stream()
                .map(invoicePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByQuoteIdAndTenantId(UUID quoteId, String tenantId) {
        return invoiceJpaRepository.existsByQuoteIdAndTenantId(quoteId, tenantId);
    }

    @Override
    public List<Invoice> findAllByStatusAndTenantId(InvoiceStatus status, String tenantId) {
        return invoiceJpaRepository.findAllByStatusAndTenantId(status, tenantId)
                .stream()
                .map(invoicePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
