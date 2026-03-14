package com.aliyara.billingservice.application.service;

import com.aliyara.billingservice.application.dto.quote.CreateQuoteRequest;
import com.aliyara.billingservice.application.dto.quote.QuoteResponse;
import com.aliyara.billingservice.application.mapper.QuoteDtoMapper;
import com.aliyara.billingservice.application.port.in.QuoteUseCase;
import com.aliyara.billingservice.domain.exception.QuoteNotFoundException;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.model.quote.QuoteLine;
import com.aliyara.billingservice.domain.port.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService implements QuoteUseCase {

    private final QuoteRepository quoteRepository;
    private final QuoteDtoMapper quoteMapper;

    @Override
    @Transactional
    public QuoteResponse createQuote(CreateQuoteRequest request) {
        log.info("Creating quote for tenantId={}, client={}", request.getTenantId(), request.getClientName());

        List<QuoteLine> domainLines = quoteMapper.toDomainLines(request.getLines());

        Quote quote = new Quote.Builder()
                .tenantId(request.getTenantId())
                .title(request.getTitle())
                .clientName(request.getClientName())
                .clientEmail(request.getClientEmail())
                .lines(domainLines)
                .build();

        Quote saved = quoteRepository.save(quote);
        return quoteMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuoteResponse getQuote(UUID id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found with id: " + id));
        return quoteMapper.toResponse(quote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuoteResponse> getQuotesByTenant(String tenantId) {
        return quoteRepository.findAllByTenantId(tenantId)
                .stream()
                .map(quoteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuoteResponse sendQuote(UUID id) {
        log.info("Transitioning quote id={} to SENT", id);
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found with id: " + id));
        quote.markAsSent();
        return quoteMapper.toResponse(quoteRepository.save(quote));
    }

    @Override
    @Transactional
    public QuoteResponse acceptQuote(UUID id) {
        log.info("Transitioning quote id={} to ACCEPTED", id);
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found with id: " + id));
        quote.markAsAccepted();
        return quoteMapper.toResponse(quoteRepository.save(quote));
    }
}
