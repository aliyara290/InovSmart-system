package com.aliyara.companyservice.adapters.out.keycloak.adapter;

import com.aliyara.companyservice.adapters.out.keycloak.client.KeycloakGroupClient;
import com.aliyara.companyservice.adapters.out.keycloak.client.KeycloakTokenClient;
import com.aliyara.companyservice.adapters.out.keycloak.client.KeycloakUserClient;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakGroupRepresentation;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakRoleRepresentation;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakTokenResponse;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakUserRepresentation;
import com.aliyara.companyservice.application.port.out.KeycloakPort;
import com.aliyara.companyservice.domain.enums.UserRole;
import com.aliyara.companyservice.domain.exception.KeycloakIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdapter implements KeycloakPort {

    private final KeycloakUserClient userClient;
    private final KeycloakGroupClient groupClient;
    private final KeycloakTokenClient tokenClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    @Override
    public String createUser(UUID tenantId, UUID companyId, String email, String password,
                             String firstName, String lastName) {
        try {
            log.info("Creating user in Keycloak: email={}, tenantId={}, companyId={}", email, tenantId, companyId);

            KeycloakUserRepresentation user = new KeycloakUserRepresentation();
            user.setUsername(email);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setEmailVerified(true);

            user.setAttributes(Map.of(
                    "tenantId", List.of(tenantId.toString()),
                    "companyId", List.of(companyId.toString())
            ));

            KeycloakUserRepresentation.KeycloakCredentialRepresentation credential =
                    new KeycloakUserRepresentation.KeycloakCredentialRepresentation("password", password, false);
            user.setCredentials(List.of(credential));

            log.debug("Sending user creation request to Keycloak");
            ResponseEntity<Void> response = userClient.createUser(user);

            if (response.getStatusCode().is2xxSuccessful()) {
                String location = response.getHeaders().getLocation().getPath();
                String userId = location.substring(location.lastIndexOf('/') + 1);
                log.info("User created successfully in Keycloak: userId={}", userId);
                return userId;
            } else {
                log.error("Keycloak user creation failed with status: {}", response.getStatusCode());
                throw new KeycloakIntegrationException("Failed to create user in Keycloak: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error creating user in Keycloak: email={}, error={}", email, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to create user in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public String createUserWithEmailVerification(UUID tenantId, UUID companyId, String email,
                                                  String firstName, String lastName) {
        try {
            log.info("Creating user with email verification in Keycloak: email={}, tenantId={}, companyId={}", email, tenantId, companyId);

            KeycloakUserRepresentation user = new KeycloakUserRepresentation();
            user.setUsername(email);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setEmailVerified(false);

            user.setAttributes(Map.of(
                    "tenantId", List.of(tenantId.toString()),
                    "companyId", List.of(companyId.toString())
            ));

            log.debug("Sending user creation request to Keycloak");
            ResponseEntity<Void> response = userClient.createUser(user);

            if (response.getStatusCode().is2xxSuccessful()) {
                String location = response.getHeaders().getLocation().getPath();
                String userId = location.substring(location.lastIndexOf('/') + 1);
                log.info("User created successfully in Keycloak: userId={}", userId);

                try {
                    log.info("Sending email verification with UPDATE_PASSWORD action to user: {}", email);
                    userClient.executeActionsEmail(userId, "frontend-client", 43200, List.of("UPDATE_PASSWORD", "VERIFY_EMAIL"));
                    log.info("Email verification sent successfully to user: {}", email);
                } catch (Exception emailException) {
                    log.warn("Failed to send email verification to user: {}. User created successfully but email not sent. Error: {}", 
                            email, emailException.getMessage());
                    log.warn("Please ensure Keycloak SMTP is configured or manually send verification email from Keycloak admin console.");
                }

                return userId;
            } else {
                log.error("Keycloak user creation failed with status: {}", response.getStatusCode());
                throw new KeycloakIntegrationException("Failed to create user in Keycloak: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error creating user with email verification in Keycloak: email={}, error={}", email, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to create user with email verification in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public void createCompanyGroup(UUID tenantId) {
        try {
            String tenantGroupName = "tenant-" + tenantId;
            log.info("Creating company group structure in Keycloak: tenantId={}, groupName={}", tenantId, tenantGroupName);

            KeycloakGroupRepresentation tenantGroup = new KeycloakGroupRepresentation();
            tenantGroup.setName(tenantGroupName);

            log.debug("Creating parent group: {}", tenantGroupName);
            ResponseEntity<Void> response = groupClient.createGroup(tenantGroup);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to create parent group with status: {}", response.getStatusCode());
                throw new KeycloakIntegrationException("Failed to create parent group: " + response.getStatusCode());
            }

            String location = response.getHeaders().getLocation().getPath();
            String tenantGroupId = location.substring(location.lastIndexOf('/') + 1);
            log.info("Parent group created: tenantGroupId={}", tenantGroupId);

            for (UserRole role : UserRole.values()) {
                log.info("Creating role subgroup: {}", role.name());
                KeycloakGroupRepresentation roleGroup = new KeycloakGroupRepresentation();
                roleGroup.setName(role.name());
                ResponseEntity<Void> subgroupResponse = groupClient.createSubgroup(tenantGroupId, roleGroup);
                String subgroupLocation = subgroupResponse.getHeaders().getLocation().toString();
                String subgroupId = subgroupLocation.substring(subgroupLocation.lastIndexOf("/") + 1);

                if (subgroupResponse.getStatusCode().is2xxSuccessful()) {
                    log.info("Subgroup created successfully: {}", role.name());
                    KeycloakRoleRepresentation getRealmRole = groupClient.getRealmRole(role.toString());
                    log.debug("keycloak real role: {}", getRealmRole);
                    KeycloakRoleRepresentation attachingBody = new KeycloakRoleRepresentation();
                    attachingBody.setId(getRealmRole.getId());
                    attachingBody.setName(getRealmRole.getName());
                    groupClient.attachRoleToGroup(subgroupId, List.of(attachingBody));
                } else {
                    log.error("Failed to create subgroup {} with status: {}", role.name(), subgroupResponse.getStatusCode());
                    throw new KeycloakIntegrationException("Failed to create subgroup: " + role.name());
                }
            }

            log.info("Company group structure created successfully for tenantId={}", tenantId);
        } catch (Exception e) {
            log.error("Error creating company group structure: tenantId={}, error={}", tenantId, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to create company group structure in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public void assignUserToGroup(String keycloakUserId, UUID tenantId, UserRole role) {
        try {
            log.info("Assigning user to group: userId={}, tenantId={}, role={}", keycloakUserId, tenantId, role);
            String roleGroupId = findRoleGroupId(tenantId, role);
            userClient.addUserToGroup(keycloakUserId, roleGroupId);
            log.info("User assigned to group successfully: userId={}, groupId={}", keycloakUserId, roleGroupId);
        } catch (Exception e) {
            log.error("Error assigning user to group: userId={}, tenantId={}, role={}, error={}",
                    keycloakUserId, tenantId, role, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to assign user to group in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public void changeUserGroup(String keycloakUserId, UUID tenantId, UserRole oldRole, UserRole newRole) {
        try {
            log.info("Changing user group: userId={}, tenantId={}, oldRole={}, newRole={}",
                    keycloakUserId, tenantId, oldRole, newRole);

            String oldRoleGroupId = findRoleGroupId(tenantId, oldRole);
            String newRoleGroupId = findRoleGroupId(tenantId, newRole);

            userClient.removeUserFromGroup(keycloakUserId, oldRoleGroupId);
            userClient.addUserToGroup(keycloakUserId, newRoleGroupId);

            log.info("User group changed successfully: userId={}, oldGroupId={}, newGroupId={}",
                    keycloakUserId, oldRoleGroupId, newRoleGroupId);
        } catch (Exception e) {
            log.error("Error changing user group: userId={}, tenantId={}, error={}",
                    keycloakUserId, tenantId, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to change user group in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeUserFromGroup(String keycloakUserId, UUID tenantId) {
        try {
            log.info("Removing user from all groups: userId={}, tenantId={}", keycloakUserId, tenantId);

            for (UserRole role : UserRole.values()) {
                try {
                    String roleGroupId = findRoleGroupId(tenantId, role);
                    userClient.removeUserFromGroup(keycloakUserId, roleGroupId);
                } catch (Exception e) {
                    log.warn("Could not remove user from role group: role={}, error={}", role, e.getMessage());
                }
            }

            log.info("User removed from all groups: userId={}", keycloakUserId);
        } catch (Exception e) {
            log.error("Error removing user from groups: userId={}, tenantId={}, error={}",
                    keycloakUserId, tenantId, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to remove user from groups in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String keycloakUserId) {
        try {
            log.info("Deleting user from Keycloak: userId={}", keycloakUserId);
            userClient.deleteUser(keycloakUserId);
            log.info("User deleted successfully: userId={}", keycloakUserId);
        } catch (Exception e) {
            log.error("Error deleting user: userId={}, error={}", keycloakUserId, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to delete user in Keycloak: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUserAccessToken(String email, String password) {
        try {
            log.info("Obtaining user access token: email={}", email);
            KeycloakTokenResponse response = tokenClient.getUserToken(
                    realm,
                    "password",
                    email,
                    password,
                    "frontend-client"
//                    clientSecret
            );
            log.info("User access token obtained successfully: email={}", email);
            return response.getAccessToken();
        } catch (Exception e) {
            log.error("Error obtaining user access token: email={}, error={}", email, e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to obtain user access token: " + e.getMessage(), e);
        }
    }

    private String findRoleGroupId(UUID tenantId, UserRole role) {
        String tenantGroupName = "tenant-" + tenantId;
        log.debug("Finding role group: tenantId={}, role={}", tenantId, role);

        List<KeycloakGroupRepresentation> groups = groupClient.searchGroups(tenantGroupName);

        if (groups.isEmpty()) {
            log.error("Tenant group not found: {}", tenantGroupName);
            throw new KeycloakIntegrationException("Tenant group not found: " + tenantGroupName);
        }

        String tenantGroupId = groups.get(0).getId();
        log.info("Fetching tenant group children: groupId={}", tenantGroupId);
        
        List<KeycloakGroupRepresentation> subGroups = groupClient.getGroupChildren(tenantGroupId);
        
        log.info("Tenant group children fetched: groupId={}, subGroupsCount={}", 
                tenantGroupId, 
                subGroups != null ? subGroups.size() : 0);

        if (subGroups == null || subGroups.isEmpty()) {
            log.error("No role subgroups found for tenant: {}", tenantId);
            throw new KeycloakIntegrationException("No role subgroups found for tenant: " + tenantId);
        }

        String roleGroupId = subGroups.stream()
                .filter(subGroup -> subGroup.getName().equals(role.name()))
                .map(KeycloakGroupRepresentation::getId)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Role group not found: role={}, tenantId={}", role, tenantId);
                    return new KeycloakIntegrationException("Role group not found: " + role);
                });

        log.debug("Found role group: role={}, groupId={}", role, roleGroupId);
        return roleGroupId;
    }
}
