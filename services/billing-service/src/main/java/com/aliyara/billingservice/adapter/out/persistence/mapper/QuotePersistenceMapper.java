package com.aliyara.billingservice.adapter.out.persistence.mapper;

import com.aliyara.billingservice.adapter.out.persistence.entity.QuoteEntity;
import com.aliyara.billingservice.adapter.out.persistence.entity.QuoteLineEntity;
import com.aliyara.billingservice.domain.model.common.CompanySnapshot;
import com.aliyara.billingservice.domain.model.common.CustomerSnapshot;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.QuoteLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuotePersistenceMapper {

    @Mapping(target = "lines", ignore = true)
    @Mapping(target = "companyName", expression = "java(quote.getCompanySnapshot() != null ? quote.getCompanySnapshot().getName() : null)")
    @Mapping(target = "companyEmail", expression = "java(quote.getCompanySnapshot() != null ? quote.getCompanySnapshot().getEmail() : null)")
    @Mapping(target = "companyPhone", expression = "java(quote.getCompanySnapshot() != null ? quote.getCompanySnapshot().getPhone() : null)")
    @Mapping(target = "companyAddress", expression = "java(quote.getCompanySnapshot() != null ? quote.getCompanySnapshot().getAddress() : null)")
    @Mapping(target = "customerName", expression = "java(quote.getCustomerSnapshot() != null ? quote.getCustomerSnapshot().getName() : null)")
    @Mapping(target = "customerEmail", expression = "java(quote.getCustomerSnapshot() != null ? quote.getCustomerSnapshot().getEmail() : null)")
    @Mapping(target = "customerPhone", expression = "java(quote.getCustomerSnapshot() != null ? quote.getCustomerSnapshot().getPhone() : null)")
    @Mapping(target = "customerAddress", expression = "java(quote.getCustomerSnapshot() != null ? quote.getCustomerSnapshot().getAddress() : null)")
    QuoteEntity toEntity(Quote quote);

    @Mapping(target = "quote", ignore = true)
    QuoteLineEntity toLineEntity(QuoteLine line);

    default QuoteEntity toEntityWithLines(Quote quote) {
        QuoteEntity entity = toEntity(quote);
        List<QuoteLineEntity> lineEntities = quote.getLines().stream()
                .map(line -> {
                    QuoteLineEntity lineEntity = toLineEntity(line);
                    lineEntity.setQuote(entity);
                    return lineEntity;
                })
                .collect(Collectors.toList());
        entity.setLines(lineEntities);
        return entity;
    }

    default Quote toDomain(QuoteEntity entity) {
        if (entity == null) return null;
        List<QuoteLine> lines = entity.getLines().stream()
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

        return new Quote.Builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .clientId(entity.getClientId())
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

    default QuoteLine toLineDomain(QuoteLineEntity entity) {
        if (entity == null) return null;
        return new QuoteLine.Builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }
}
