package com.aliyara.billingservice.application.mapper;

import com.aliyara.billingservice.application.dto.quote.QuoteLineRequest;
import com.aliyara.billingservice.application.dto.quote.QuoteLineResponse;
import com.aliyara.billingservice.application.dto.quote.QuoteResponse;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.QuoteLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuoteDtoMapper {

    QuoteLineResponse toLineResponse(QuoteLine line);

    List<QuoteLineResponse> toLineResponseList(List<QuoteLine> lines);

    QuoteResponse toResponse(Quote quote);

    default List<QuoteLine> toDomainLines(List<QuoteLineRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(r -> new QuoteLine.Builder()
                        .productName(r.getProductName())
                        .description(r.getDescription())
                        .quantity(r.getQuantity())
                        .unitPrice(r.getUnitPrice())
                        .taxRate(r.getTaxRate())
                        .build())
                .toList();
    }
}
