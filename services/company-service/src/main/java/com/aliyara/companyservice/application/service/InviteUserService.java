package com.aliyara.companyservice.application.service;

import com.aliyara.companyservice.application.port.in.InviteUserUseCase;
import com.aliyara.companyservice.application.port.out.CompanyInvitationRepositoryPort;
import com.aliyara.companyservice.application.port.out.CompanyRepositoryPort;
import com.aliyara.companyservice.application.port.out.CompanyUserRepositoryPort;
import com.aliyara.companyservice.application.port.out.KeycloakPort;
import com.aliyara.companyservice.domain.enums.InvitationStatus;
import com.aliyara.companyservice.domain.enums.UserStatus;
import com.aliyara.companyservice.domain.exception.InvitationAlreadyAcceptedException;
import com.aliyara.companyservice.domain.exception.InvitationExpiredException;
import com.aliyara.companyservice.domain.exception.InvitationNotFoundException;
import com.aliyara.companyservice.domain.exception.UserAlreadyExistsException;
import com.aliyara.companyservice.domain.model.Company;
import com.aliyara.companyservice.domain.model.CompanyInvitation;
import com.aliyara.companyservice.domain.model.CompanyUser;
import com.aliyara.companyservice.infrastructure.security.TenantSecurityService;
import com.aliyara.companyservice.interfaces.dto.request.AcceptInvitationRequest;
import com.aliyara.companyservice.interfaces.dto.request.InviteUserRequest;
import com.aliyara.companyservice.interfaces.dto.response.RegisterCompanyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InviteUserService implements InviteUserUseCase {

    private final CompanyInvitationRepositoryPort invitationRepository;
    private final CompanyUserRepositoryPort companyUserRepository;
    private final CompanyRepositoryPort companyRepository;
    private final KeycloakPort keycloakPort;
    private final TenantSecurityService tenantSecurityService;

    @Value("${invitation.expiry-hours:48}")
    private int invitationExpiryHours;

    public InviteUserService(CompanyInvitationRepositoryPort invitationRepository,
                            CompanyUserRepositoryPort companyUserRepository,
                            CompanyRepositoryPort companyRepository,
                            KeycloakPort keycloakPort,
                            TenantSecurityService tenantSecurityService) {
        this.invitationRepository = invitationRepository;
        this.companyUserRepository = companyUserRepository;
        this.companyRepository = companyRepository;
        this.keycloakPort = keycloakPort;
        this.tenantSecurityService = tenantSecurityService;
    }

    @Override
    @Transactional
    public void invite(UUID tenantId, InviteUserRequest request) {
        tenantSecurityService.validateTenantAccess(tenantId);

        if (companyUserRepository.existsByTenantIdAndEmail(tenantId, request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        Company company = companyRepository.findByTenantId(tenantId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        String keycloakUserId = keycloakPort.createUserWithEmailVerification(
            tenantId,
            company.getId(),
            request.getEmail(),
            request.getFirstName(),
            request.getLastName()
        );

        keycloakPort.assignUserToGroup(keycloakUserId, tenantId, request.getRole());

        CompanyUser companyUser = new CompanyUser();
        companyUser.setId(UUID.randomUUID());
        companyUser.setTenantId(tenantId);
        companyUser.setCompanyId(company.getId());
        companyUser.setUserId(UUID.fromString(keycloakUserId));
        companyUser.setEmail(request.getEmail());
        companyUser.setFirstName(request.getFirstName());
        companyUser.setLastName(request.getLastName());
        companyUser.setStatus(UserStatus.INVITED);
        companyUser.setCreatedAt(LocalDateTime.now());

        companyUserRepository.save(companyUser);

        CompanyInvitation invitation = new CompanyInvitation();
        invitation.setId(UUID.randomUUID());
        invitation.setTenantId(tenantId);
        invitation.setCompanyId(company.getId());
        invitation.setEmail(request.getEmail());
        invitation.setRole(request.getRole());
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setExpiresAt(LocalDateTime.now().plusHours(invitationExpiryHours));
        invitation.setCreatedAt(LocalDateTime.now());

        invitationRepository.save(invitation);
    }

    @Override
    @Transactional
    @Deprecated
    public RegisterCompanyResponse acceptInvitation(AcceptInvitationRequest request) {
        throw new UnsupportedOperationException(
            "This endpoint is deprecated. Users should verify email and set password via Keycloak email link."
        );
    }
}
