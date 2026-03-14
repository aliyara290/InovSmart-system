package com.aliyara.billingservice.adapter.out.persistence;

import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceEntity;
import com.aliyara.billingservice.adapter.out.persistence.jpa.InvoiceJpaRepository;
import com.aliyara.billingservice.adapter.out.persistence.mapper.InvoicePersistenceMapper;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.port.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceRepositoryAdapter implements InvoiceRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final InvoicePersistenceMapper mapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = mapper.toEntity(invoice);
        InvoiceEntity saved = invoiceJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Invoice> findById(UUID id) {
        return invoiceJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Invoice> findAllByTenantId(String tenantId) {
        return invoiceJpaRepository.findAllByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
