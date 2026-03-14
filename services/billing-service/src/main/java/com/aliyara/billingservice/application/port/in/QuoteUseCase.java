package com.aliyara.billingservice.application.port.in;

import com.aliyara.billingservice.application.dto.quote.CreateQuoteRequest;
import com.aliyara.billingservice.application.dto.quote.QuoteResponse;

import java.util.List;
import java.util.UUID;

public interface QuoteUseCase {
    QuoteResponse createQuote(CreateQuoteRequest request);

    QuoteResponse getQuote(UUID id);

    List<QuoteResponse> getQuotesByTenant(String tenantId);

    QuoteResponse sendQuote(UUID id);

    QuoteResponse acceptQuote(UUID id);
}
