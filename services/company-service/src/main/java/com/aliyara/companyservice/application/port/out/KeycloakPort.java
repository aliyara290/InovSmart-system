package com.aliyara.companyservice.application.port.out;

import com.aliyara.companyservice.domain.enums.UserRole;

import java.util.UUID;

public interface KeycloakPort {
    String createUser(UUID tenantId, UUID companyId, String email, String password, String firstName, String lastName);
    void createCompanyGroup(UUID tenantId);
    void assignUserToGroup(String keycloakUserId, UUID tenantId, UserRole role);
    void changeUserGroup(String keycloakUserId, UUID tenantId, UserRole oldRole, UserRole newRole);
    void removeUserFromGroup(String keycloakUserId, UUID tenantId);
    void deleteUser(String keycloakUserId);
    String getUserAccessToken(String email, String password);
}
