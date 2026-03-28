package com.aliyara.companyservice.application.service;

import com.aliyara.companyservice.application.port.in.ManageCompanyUseCase;
import com.aliyara.companyservice.application.port.out.CompanyRepositoryPort;
import com.aliyara.companyservice.domain.enums.CompanyStatus;
import com.aliyara.companyservice.domain.exception.CompanyNotFoundException;
import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.infrastructure.mapper.CompanyMapper;
import com.aliyara.companyservice.infrastructure.security.TenantSecurityService;
import com.aliyara.companyservice.interfaces.dto.request.UpdateCompanyRequest;
import com.aliyara.companyservice.interfaces.dto.response.CompanyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ManageCompanyService implements ManageCompanyUseCase {

    private final CompanyRepositoryPort companyRepository;
    private final CompanyMapper companyMapper;
    private final TenantSecurityService tenantSecurityService;

    public ManageCompanyService(CompanyRepositoryPort companyRepository,
                               CompanyMapper companyMapper,
                               TenantSecurityService tenantSecurityService) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.tenantSecurityService = tenantSecurityService;
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getByTenantId(UUID tenantId) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        Company company = companyRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new CompanyNotFoundException(tenantId));
        
        return companyMapper.toResponse(company);
    }

    @Override
    @Transactional
    public CompanyResponse update(UUID tenantId, UpdateCompanyRequest request) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        Company company = companyRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new CompanyNotFoundException(tenantId));
        
        company.setName(request.getName());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setAddress(request.getAddress());
        company.setIndustry(request.getIndustry());
        
        if (request.getSubscriptionPlan() != null) {
            company.setSubscriptionPlan(request.getSubscriptionPlan());
        }
        
        company.setUpdatedAt(LocalDateTime.now());
        
        Company updatedCompany = companyRepository.save(company);
        
        return companyMapper.toResponse(updatedCompany);
    }

    @Override
    @Transactional
    public void updateStatus(UUID tenantId, CompanyStatus status) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        Company company = companyRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new CompanyNotFoundException(tenantId));
        
        company.setStatus(status);
        company.setUpdatedAt(LocalDateTime.now());
        
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void delete(UUID tenantId) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        Company company = companyRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new CompanyNotFoundException(tenantId));
        
        company.setStatus(CompanyStatus.DELETED);
        company.setUpdatedAt(LocalDateTime.now());
        
        companyRepository.save(company);
    }
}
