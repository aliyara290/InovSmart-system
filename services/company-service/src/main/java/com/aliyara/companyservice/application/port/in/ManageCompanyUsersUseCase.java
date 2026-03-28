package com.aliyara.companyservice.application.port.in;

import com.aliyara.companyservice.domain.enums.UserRole;
import com.aliyara.companyservice.domain.enums.UserStatus;
import com.aliyara.companyservice.interfaces.dto.response.CompanyUserResponse;

import java.util.List;
import java.util.UUID;

public interface ManageCompanyUsersUseCase {
    List<CompanyUserResponse> getUsersByTenantId(UUID tenantId);
    CompanyUserResponse getUserById(UUID tenantId, UUID userId);
    void changeUserRole(UUID tenantId, UUID userId, UserRole newRole);
    void changeUserStatus(UUID tenantId, UUID userId, UserStatus status);
    void removeUser(UUID tenantId, UUID userId);
}
