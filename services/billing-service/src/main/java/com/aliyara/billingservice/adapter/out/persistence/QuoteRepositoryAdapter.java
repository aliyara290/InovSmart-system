package com.aliyara.billingservice.adapter.out.persistence;

import com.aliyara.billingservice.adapter.out.persistence.entity.QuoteEntity;
import com.aliyara.billingservice.adapter.out.persistence.jpa.QuoteJpaRepository;
import com.aliyara.billingservice.adapter.out.persistence.mapper.QuotePersistenceMapper;
import com.aliyara.billingservice.domain.model.quote.Quote;
import com.aliyara.billingservice.domain.port.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuoteRepositoryAdapter implements QuoteRepository {

    private final QuoteJpaRepository quoteJpaRepository;
    private final QuotePersistenceMapper mapper;

    @Override
    public Quote save(Quote quote) {
        QuoteEntity entity = mapper.toEntity(quote);
        QuoteEntity saved = quoteJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Quote> findById(UUID id) {
        return quoteJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Quote> findAllByTenantId(String tenantId) {
        return quoteJpaRepository.findAllByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
