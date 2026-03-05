package com.aliyara.companyservice.infrastrecture.adapter.persistence;

import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;
import com.aliyara.companyservice.domain.port.CompanyRepository;
import com.aliyara.companyservice.infrastrecture.adapter.persistence.entity.CompanyEntity;
import com.aliyara.companyservice.infrastrecture.adapter.persistence.mapper.CompanyPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyPersistenceMapper companyPersistenceMapper;

    @Override
    public Company save(Company company) {
        CompanyEntity entity = companyPersistenceMapper.toEntity(company);
        CompanyEntity savedEntity = companyJpaRepository.save(entity);
        return companyPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Company> existById(CompanyId id) {
        return findById(id);
    }

    @Override
    public Optional<Company> findById(CompanyId id) {
        return companyJpaRepository.findById(id.id())
                .map(companyPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Company> findByTenantId(TenantId tenantId) {
        return companyJpaRepository.findByTenantId(tenantId.id())
                .map(companyPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(CompanyId companyId) {
        companyJpaRepository.deleteById(companyId.id());
    }
}
