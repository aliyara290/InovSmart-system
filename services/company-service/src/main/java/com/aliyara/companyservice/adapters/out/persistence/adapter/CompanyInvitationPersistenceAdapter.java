package com.aliyara.companyservice.adapters.out.persistence.adapter;

import com.aliyara.companyservice.adapters.out.persistence.repository.CompanyInvitationJpaRepository;
import com.aliyara.companyservice.application.port.out.CompanyInvitationRepositoryPort;
import com.aliyara.companyservice.domain.model.CompanyInvitation;
import com.aliyara.companyservice.infrastructure.mapper.CompanyInvitationMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CompanyInvitationPersistenceAdapter implements CompanyInvitationRepositoryPort {

    private final CompanyInvitationJpaRepository repository;
    private final CompanyInvitationMapper mapper;

    public CompanyInvitationPersistenceAdapter(CompanyInvitationJpaRepository repository, 
                                               CompanyInvitationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompanyInvitation save(CompanyInvitation invitation) {
        return mapper.toDomain(
            repository.save(mapper.toJpaEntity(invitation))
        );
    }

    @Override
    public Optional<CompanyInvitation> findByToken(String token) {
        return repository.findByToken(token)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<CompanyInvitation> findByTenantIdAndEmail(UUID tenantId, String email) {
        return repository.findByTenantIdAndEmail(tenantId, email)
            .map(mapper::toDomain);
    }
}
