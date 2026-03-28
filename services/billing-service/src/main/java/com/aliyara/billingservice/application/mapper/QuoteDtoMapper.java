package com.aliyara.billingservice.application.mapper;

import com.aliyara.billingservice.application.dto.quote.QuoteLineResponse;
import com.aliyara.billingservice.application.dto.quote.QuoteResponse;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.QuoteLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuoteDtoMapper {

    QuoteResponse toResponse(Quote quote);

    QuoteLineResponse toLineResponse(QuoteLine line);

    List<QuoteLineResponse> toLineResponses(List<QuoteLine> lines);
}
