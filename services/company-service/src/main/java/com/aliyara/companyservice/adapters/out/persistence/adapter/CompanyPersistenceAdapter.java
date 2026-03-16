package com.aliyara.companyservice.adapters.out.persistence.adapter;

import com.aliyara.companyservice.adapters.out.persistence.repository.CompanyJpaRepository;
import com.aliyara.companyservice.application.port.out.CompanyRepositoryPort;
import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.infrastructure.mapper.CompanyMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CompanyPersistenceAdapter implements CompanyRepositoryPort {

    private final CompanyJpaRepository repository;
    private final CompanyMapper mapper;

    public CompanyPersistenceAdapter(CompanyJpaRepository repository, CompanyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Company save(Company company) {
        return mapper.toDomain(
            repository.save(mapper.toJpaEntity(company))
        );
    }

    @Override
    public Optional<Company> findByTenantId(UUID tenantId) {
        return repository.findByTenantId(tenantId)
            .map(mapper::toDomain);
    }

    @Override
    public void delete(Company company) {
        repository.delete(mapper.toJpaEntity(company));
    }

    @Override
    public boolean existsByTenantId(UUID tenantId) {
        return repository.existsByTenantId(tenantId);
    }
}
