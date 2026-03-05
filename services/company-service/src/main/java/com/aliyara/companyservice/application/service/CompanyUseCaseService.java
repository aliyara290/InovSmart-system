package com.aliyara.companyservice.application.service;

import com.aliyara.companyservice.application.dto.CompanyRequest;
import com.aliyara.companyservice.application.dto.CompanyResponse;
import com.aliyara.companyservice.application.dto.CompanyUserResponse;
import com.aliyara.companyservice.application.mapper.CompanyDtoMapper;
import com.aliyara.companyservice.domain.exception.CompanyNotFoundException;
import com.aliyara.companyservice.domain.exception.DuplicateTenantException;
import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.User;
import com.aliyara.companyservice.domain.model.ids.CompanyId;
import com.aliyara.companyservice.domain.model.ids.TenantId;
import com.aliyara.companyservice.domain.port.CompanyRepository;
import com.aliyara.companyservice.domain.port.KeycloakPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyUseCaseService {

    private final CompanyRepository companyRepository;
    private final KeycloakPort keycloakPort;
    private final CompanyDtoMapper companyDtoMapper;

    @Transactional
    public CompanyResponse registerCompany(CompanyRequest request) {

        Company companyToSave = companyDtoMapper.toDomain(request);
        Company savedCompany = companyRepository.save(companyToSave);

        keycloakPort.createTenantGroup(companyToSave.getTenantId());

        return companyDtoMapper.toResponse(savedCompany);
    }

    @Transactional(readOnly = true)
    public List<CompanyUserResponse> getCompanyUsers(CompanyId companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + companyId.id()));

        List<User> users = keycloakPort.getUsersByTenantId(company.getTenantId());

        return users.stream()
                .map(companyDtoMapper::toUserResponse)
                .collect(Collectors.toList());
    }
}
