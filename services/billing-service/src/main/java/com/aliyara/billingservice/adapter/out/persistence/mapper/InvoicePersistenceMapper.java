package com.aliyara.billingservice.adapter.out.persistence.mapper;

import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceEntity;
import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceLineEntity;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.InvoiceLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoicePersistenceMapper {

    // ── Entity → Domain ───────────────────────────────────────────────────────

    default InvoiceLine toLineDomain(InvoiceLineEntity entity) {
        if (entity == null) {
            return null;
        }
        return new InvoiceLine.Builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .taxRate(entity.getTaxRate())
                .build();
    }

    default Invoice toDomain(InvoiceEntity entity) {
        if (entity == null) {
            return null;
        }
        List<InvoiceLine> lines = entity.getLines() == null ? List.of()
                : entity.getLines().stream().map(this::toLineDomain).toList();

        return new Invoice.Builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .tenantId(entity.getTenantId())
                .quoteId(entity.getQuoteId())
                .invoiceNumber(entity.getInvoiceNumber())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .dueDate(entity.getDueDate())
                .lines(lines)
                .build();
    }

    // ── Domain → Entity ───────────────────────────────────────────────────────

    default InvoiceLineEntity toLineEntity(InvoiceLine line, InvoiceEntity parentInvoice) {
        if (line == null) {
            return null;
        }
        InvoiceLineEntity entity = new InvoiceLineEntity();
        entity.setId(line.getId());
        entity.setProductName(line.getProductName());
        entity.setDescription(line.getDescription());
        entity.setQuantity(line.getQuantity());
        entity.setUnitPrice(line.getUnitPrice());
        entity.setTaxRate(line.getTaxRate());
        entity.setLineTotal(line.getLineTotal());
        entity.setInvoice(parentInvoice);
        return entity;
    }

    default InvoiceEntity toEntity(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoice.getId());
        entity.setTitle(invoice.getTitle());
        entity.setTenantId(invoice.getTenantId());
        entity.setQuoteId(invoice.getQuoteId());
        entity.setInvoiceNumber(invoice.getInvoiceNumber());
        entity.setStatus(invoice.getStatus());
        entity.setTotalHT(invoice.getTotalHT());
        entity.setTotalTTC(invoice.getTotalTTC());
        entity.setCreatedAt(invoice.getCreatedAt());
        entity.setDueDate(invoice.getDueDate());

        List<InvoiceLineEntity> lineEntities = invoice.getLines().stream()
                .map(line -> toLineEntity(line, entity))
                .toList();

        entity.setLines(lineEntities);
        return entity;
    }
}
