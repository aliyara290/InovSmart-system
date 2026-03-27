package com.aliyara.billingservice.application.port.in;

import com.aliyara.billingservice.application.dto.quote.CreateQuoteRequest;
import com.aliyara.billingservice.application.dto.quote.QuoteResponse;
import com.aliyara.billingservice.application.dto.quote.UpdateQuoteRequest;

import java.util.List;
import java.util.UUID;

public interface QuoteUseCase {

    QuoteResponse createQuote(CreateQuoteRequest request);

    QuoteResponse updateQuote(UUID quoteId, UpdateQuoteRequest request);

    QuoteResponse getQuote(UUID quoteId);

    List<QuoteResponse> getAllQuotes();

    QuoteResponse sendQuote(UUID quoteId);

    QuoteResponse acceptQuote(UUID quoteId);

    QuoteResponse rejectQuote(UUID quoteId);

    QuoteResponse generateQuoteDocument(UUID quoteId);
}
