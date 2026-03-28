package com.aliyara.companyservice.application.service;

import com.aliyara.companyservice.application.port.in.RegisterCompanyUseCase;
import com.aliyara.companyservice.application.port.out.CompanyRepositoryPort;
import com.aliyara.companyservice.application.port.out.CompanyUserRepositoryPort;
import com.aliyara.companyservice.application.port.out.KeycloakPort;
import com.aliyara.companyservice.domain.enums.CompanyStatus;
import com.aliyara.companyservice.domain.enums.UserRole;
import com.aliyara.companyservice.domain.enums.UserStatus;
import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.CompanyUser;
import com.aliyara.companyservice.infrastructure.mapper.CompanyMapper;
import com.aliyara.companyservice.interfaces.dto.request.RegisterCompanyRequest;
import com.aliyara.companyservice.interfaces.dto.response.RegisterCompanyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterCompanyService implements RegisterCompanyUseCase {

    private final CompanyRepositoryPort companyRepository;
    private final CompanyUserRepositoryPort companyUserRepository;
    private final KeycloakPort keycloakPort;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional
    public RegisterCompanyResponse register(RegisterCompanyRequest request) {
        UUID tenantId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        String keycloakUserId = null;
        Company savedCompany = null;

        try {
            keycloakUserId = keycloakPort.createUser(
                tenantId,
                companyId,
                request.getOwner().getEmail(),
                request.getOwner().getPassword(),
                request.getOwner().getFirstName(),
                request.getOwner().getLastName()
            );

            UUID ownerKeycloakId = UUID.fromString(keycloakUserId);

            Company company = companyMapper.toDomain(request.getCompany());
            company.setId(companyId);
            company.setTenantId(tenantId);
            company.setOwnerId(ownerKeycloakId);
            company.setStatus(CompanyStatus.PENDING);
            company.setCreatedAt(LocalDateTime.now());
            company.setUpdatedAt(LocalDateTime.now());

            savedCompany = companyRepository.save(company);

            keycloakPort.createCompanyGroup(tenantId);

            keycloakPort.assignUserToGroup(keycloakUserId, tenantId, UserRole.OWNER);

            CompanyUser companyUser = new CompanyUser();
            companyUser.setId(UUID.randomUUID());
            companyUser.setTenantId(tenantId);
            companyUser.setCompanyId(companyId);
            companyUser.setUserId(ownerKeycloakId);
            companyUser.setEmail(request.getOwner().getEmail());
            companyUser.setFirstName(request.getOwner().getFirstName());
            companyUser.setLastName(request.getOwner().getLastName());
            companyUser.setStatus(UserStatus.ACTIVE);
            companyUser.setJoinedAt(LocalDateTime.now());
            companyUser.setCreatedAt(LocalDateTime.now());

            companyUserRepository.save(companyUser);

            savedCompany.setStatus(CompanyStatus.ACTIVE);
            companyRepository.save(savedCompany);

            String accessToken = keycloakPort.getUserAccessToken(
                request.getOwner().getEmail(),
                request.getOwner().getPassword()
            );

            RegisterCompanyResponse response = new RegisterCompanyResponse();
            response.setTenantId(tenantId);
            response.setCompanyId(companyId);
            response.setUserId(ownerKeycloakId);
            response.setAccessToken(accessToken);

            return response;

        } catch (Exception e) {
            rollbackRegistration(savedCompany, keycloakUserId);
            throw new RuntimeException("Company registration failed: " + e.getMessage(), e);
        }
    }

    private void rollbackRegistration(Company company, String keycloakUserId) {
        try {
            if (keycloakUserId != null) {
                try {
                    keycloakPort.deleteUser(keycloakUserId);
                } catch (Exception e) {
                }
            }

            if (company != null && company.getId() != null) {
                try {
                    companyRepository.delete(company);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
