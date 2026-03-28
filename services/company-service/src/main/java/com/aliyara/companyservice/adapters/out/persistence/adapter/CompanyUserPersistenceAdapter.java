package com.aliyara.companyservice.adapters.out.persistence.adapter;

import com.aliyara.companyservice.adapters.out.persistence.repository.CompanyUserJpaRepository;
import com.aliyara.companyservice.application.port.out.CompanyUserRepositoryPort;
import com.aliyara.companyservice.domain.model.CompanyUser;
import com.aliyara.companyservice.infrastructure.mapper.CompanyUserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CompanyUserPersistenceAdapter implements CompanyUserRepositoryPort {

    private final CompanyUserJpaRepository repository;
    private final CompanyUserMapper mapper;

    public CompanyUserPersistenceAdapter(CompanyUserJpaRepository repository, CompanyUserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompanyUser save(CompanyUser companyUser) {
        return mapper.toDomain(
            repository.save(mapper.toJpaEntity(companyUser))
        );
    }

    @Override
    public Optional<CompanyUser> findById(UUID id) {
        return repository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<CompanyUser> findByTenantIdAndUserId(UUID tenantId, UUID userId) {
        return repository.findByTenantIdAndUserId(tenantId, userId)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<CompanyUser> findByTenantIdAndEmail(UUID tenantId, String email) {
        return repository.findByTenantIdAndEmail(tenantId, email)
            .map(mapper::toDomain);
    }

    @Override
    public List<CompanyUser> findByTenantId(UUID tenantId) {
        return repository.findByTenantId(tenantId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(CompanyUser companyUser) {
        repository.delete(mapper.toJpaEntity(companyUser));
    }

    @Override
    public boolean existsByTenantIdAndEmail(UUID tenantId, String email) {
        return repository.existsByTenantIdAndEmail(tenantId, email);
    }
}
