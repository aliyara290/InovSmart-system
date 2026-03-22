package com.aliyara.billingservice.adapter.out.persistence.mapper;

import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceEntity;
import com.aliyara.billingservice.adapter.out.persistence.entity.InvoiceLineEntity;
import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import com.aliyara.billingservice.domain.model.common.CustomerSnapshot;
import com.aliyara.billingservice.domain.model.invoice.Invoice;
import com.aliyara.billingservice.domain.model.invoice.InvoiceLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface InvoicePersistenceMapper {

    @Mapping(target = "lines", ignore = true)
    @Mapping(target = "companyName", expression = "java(invoice.getCompanySnapshot() != null ? invoice.getCompanySnapshot().getName() : null)")
    @Mapping(target = "companyEmail", expression = "java(invoice.getCompanySnapshot() != null ? invoice.getCompanySnapshot().getEmail() : null)")
    @Mapping(target = "companyPhone", expression = "java(invoice.getCompanySnapshot() != null ? invoice.getCompanySnapshot().getPhone() : null)")
    @Mapping(target = "companyAddress", expression = "java(invoice.getCompanySnapshot() != null ? invoice.getCompanySnapshot().getAddress() : null)")
    @Mapping(target = "customerName", expression = "java(invoice.getCustomerSnapshot() != null ? invoice.getCustomerSnapshot().getName() : null)")
    @Mapping(target = "customerEmail", expression = "java(invoice.getCustomerSnapshot() != null ? invoice.getCustomerSnapshot().getEmail() : null)")
    @Mapping(target = "customerPhone", expression = "java(invoice.getCustomerSnapshot() != null ? invoice.getCustomerSnapshot().getPhone() : null)")
    @Mapping(target = "customerAddress", expression = "java(invoice.getCustomerSnapshot() != null ? invoice.getCustomerSnapshot().getAddress() : null)")
    InvoiceEntity toEntity(Invoice invoice);

    @Mapping(target = "invoice", ignore = true)
    InvoiceLineEntity toLineEntity(InvoiceLine line);

    default InvoiceEntity toEntityWithLines(Invoice invoice) {
        InvoiceEntity entity = toEntity(invoice);
        List<InvoiceLineEntity> lineEntities = invoice.getLines().stream()
                .map(line -> {
                    InvoiceLineEntity lineEntity = toLineEntity(line);
                    lineEntity.setInvoice(entity);
                    return lineEntity;
                })
                .collect(Collectors.toList());
        entity.setLines(lineEntities);
        return entity;
    }

    default Invoice toDomain(InvoiceEntity entity) {
        if (entity == null) return null;
        List<InvoiceLine> lines = entity.getLines().stream()
                .map(this::toLineDomain)
                .collect(Collectors.toList());

        CompanySnapshot companySnapshot = null;
        if (entity.getCompanyName() != null) {
            companySnapshot = new CompanySnapshot(
                    entity.getCompanyName(),
                    entity.getCompanyEmail(),
                    entity.getCompanyPhone(),
                    entity.getCompanyAddress()
            );
        }

        CustomerSnapshot customerSnapshot = null;
        if (entity.getCustomerName() != null) {
            customerSnapshot = new CustomerSnapshot(
                    entity.getCustomerName(),
                    entity.getCustomerEmail(),
                    entity.getCustomerPhone(),
                    entity.getCustomerAddress()
            );
        }

        return new Invoice.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .clientId(entity.getClientId())
                .quoteId(entity.getQuoteId())
                .status(entity.getStatus())
                .taxRate(entity.getTaxRate())
                .lines(lines)
                .companySnapshot(companySnapshot)
                .customerSnapshot(customerSnapshot)
                .documentStatus(entity.getDocumentStatus())
                .documentUrl(entity.getDocumentUrl())
                .generatedAt(entity.getGeneratedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    default InvoiceLine toLineDomain(InvoiceLineEntity entity) {
        if (entity == null) return null;
        return new InvoiceLine.Builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }
}
