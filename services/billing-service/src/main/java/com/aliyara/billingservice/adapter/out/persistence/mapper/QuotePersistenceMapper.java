package com.aliyara.billingservice.adapter.out.persistence.mapper;

import com.aliyara.billingservice.adapter.out.persistence.entity.QuoteEntity;
import com.aliyara.billingservice.adapter.out.persistence.entity.QuoteLineEntity;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.QuoteLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuotePersistenceMapper {

    // ── Entity → Domain ───────────────────────────────────────────────────────

    default QuoteLine toLineDomain(QuoteLineEntity entity) {
        if (entity == null) {
            return null;
        }
        return new QuoteLine.Builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .taxRate(entity.getTaxRate())
                .build();
    }

    default Quote toDomain(QuoteEntity entity) {
        if (entity == null) {
            return null;
        }
        List<QuoteLine> lines = entity.getLines() == null ? List.of()
                : entity.getLines().stream().map(this::toLineDomain).toList();

        return new Quote.Builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .tenantId(entity.getTenantId())
                .clientName(entity.getClientName())
                .clientEmail(entity.getClientEmail())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .validityDate(entity.getValidityDate())
                .lines(lines)
                .build();
        // totalHT and totalTTC are calculated in the domain logic
    }

    // ── Domain → Entity ───────────────────────────────────────────────────────

    default QuoteLineEntity toLineEntity(QuoteLine line, QuoteEntity parentQuote) {
        if (line == null) {
            return null;
        }
        QuoteLineEntity entity = new QuoteLineEntity();
        entity.setId(line.getId());
        entity.setProductName(line.getProductName());
        entity.setDescription(line.getDescription());
        entity.setQuantity(line.getQuantity());
        entity.setUnitPrice(line.getUnitPrice());
        entity.setTaxRate(line.getTaxRate());
        entity.setLineTotal(line.getLineTotal());
        entity.setQuote(parentQuote);
        return entity;
    }

    default QuoteEntity toEntity(Quote quote) {
        if (quote == null) {
            return null;
        }
        QuoteEntity entity = new QuoteEntity();
        entity.setId(quote.getId());
        entity.setTitle(quote.getTitle());
        entity.setTenantId(quote.getTenantId());
        entity.setClientName(quote.getClientName());
        entity.setClientEmail(quote.getClientEmail());
        entity.setStatus(quote.getStatus());
        entity.setTotalHT(quote.getTotalHT());
        entity.setTotalTTC(quote.getTotalTTC());
        entity.setCreatedAt(quote.getCreatedAt());
        entity.setValidityDate(quote.getValidityDate());

        List<QuoteLineEntity> lineEntities = quote.getLines().stream()
                .map(line -> toLineEntity(line, entity))
                .toList();

        entity.setLines(lineEntities);
        return entity;
    }
}
