package com.aliyara.companyservice.application.service;

import com.aliyara.companyservice.application.port.in.ManageCompanyUsersUseCase;
import com.aliyara.companyservice.application.port.out.CompanyUserRepositoryPort;
import com.aliyara.companyservice.application.port.out.KeycloakPort;
import com.aliyara.companyservice.domain.enums.UserRole;
import com.aliyara.companyservice.domain.enums.UserStatus;
import com.aliyara.companyservice.domain.exception.UserNotFoundException;
import com.aliyara.companyservice.domain.model.CompanyUser;
import com.aliyara.companyservice.infrastructure.mapper.CompanyUserMapper;
import com.aliyara.companyservice.infrastructure.security.TenantSecurityService;
import com.aliyara.companyservice.interfaces.dto.response.CompanyUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ManageCompanyUsersService implements ManageCompanyUsersUseCase {

    private final CompanyUserRepositoryPort companyUserRepository;
    private final KeycloakPort keycloakPort;
    private final CompanyUserMapper companyUserMapper;
    private final TenantSecurityService tenantSecurityService;

    public ManageCompanyUsersService(CompanyUserRepositoryPort companyUserRepository,
                                    KeycloakPort keycloakPort,
                                    CompanyUserMapper companyUserMapper,
                                    TenantSecurityService tenantSecurityService) {
        this.companyUserRepository = companyUserRepository;
        this.keycloakPort = keycloakPort;
        this.companyUserMapper = companyUserMapper;
        this.tenantSecurityService = tenantSecurityService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyUserResponse> getUsersByTenantId(UUID tenantId) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        List<CompanyUser> users = companyUserRepository.findByTenantId(tenantId);
        
        return users.stream()
            .map(companyUserMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyUserResponse getUserById(UUID tenantId, UUID userId) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        CompanyUser user = companyUserRepository.findByTenantIdAndUserId(tenantId, userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        return companyUserMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void changeUserRole(UUID tenantId, UUID userId, UserRole newRole) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        CompanyUser user = companyUserRepository.findByTenantIdAndUserId(tenantId, userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        String keycloakUserId = user.getUserId().toString();
        
        UserRole oldRole = determineCurrentRole(keycloakUserId, tenantId);
        
        keycloakPort.changeUserGroup(keycloakUserId, tenantId, oldRole, newRole);
    }

    @Override
    @Transactional
    public void changeUserStatus(UUID tenantId, UUID userId, UserStatus status) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        CompanyUser user = companyUserRepository.findByTenantIdAndUserId(tenantId, userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        user.setStatus(status);
        
        companyUserRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUser(UUID tenantId, UUID userId) {
        tenantSecurityService.validateTenantAccess(tenantId);
        
        CompanyUser user = companyUserRepository.findByTenantIdAndUserId(tenantId, userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        user.setStatus(UserStatus.REMOVED);
        
        companyUserRepository.save(user);
        
        String keycloakUserId = user.getUserId().toString();
        keycloakPort.removeUserFromGroup(keycloakUserId, tenantId);
    }

    private UserRole determineCurrentRole(String keycloakUserId, UUID tenantId) {
        return UserRole.OWNER;
    }
}
