package com.aliyara.billingservice.adapter.out.persistence;

import com.aliyara.billingservice.adapter.out.persistence.entity.QuoteEntity;
import com.aliyara.billingservice.adapter.out.persistence.jpa.QuoteJpaRepository;
import com.aliyara.billingservice.adapter.out.persistence.mapper.QuotePersistenceMapper;
import com.aliyara.billingservice.application.port.out.QuoteRepositoryPort;
import com.aliyara.billingservice.domain.model.quote.Quote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuoteRepositoryAdapter implements QuoteRepositoryPort {

    private final QuoteJpaRepository quoteJpaRepository;
    private final QuotePersistenceMapper quotePersistenceMapper;

    @Override
    public Quote save(Quote quote) {
        QuoteEntity entity = quotePersistenceMapper.toEntityWithLines(quote);
        QuoteEntity saved = quoteJpaRepository.save(entity);
        return quotePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Quote> findById(UUID id) {
        return quoteJpaRepository.findById(id)
                .map(quotePersistenceMapper::toDomain);
    }

    @Override
    public List<Quote> findAllByTenantId(String tenantId) {
        return quoteJpaRepository.findAllByTenantId(tenantId).stream()
                .map(quotePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return quoteJpaRepository.existsById(id);
    }
}
