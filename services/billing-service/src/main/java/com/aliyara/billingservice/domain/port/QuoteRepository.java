package com.aliyara.billingservice.domain.port;

import com.aliyara.billingservice.domain.model.quote.Quote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteRepository {
    Quote save(Quote quote);

    Optional<Quote> findById(UUID id);

    List<Quote> findAllByTenantId(String tenantId);
}
