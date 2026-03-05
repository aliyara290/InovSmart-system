package com.aliyara.companyservice.domain.port;

import com.aliyara.companyservice.domain.model.User;
import com.aliyara.companyservice.domain.model.ids.TenantId;

import java.util.List;
import java.util.Optional;

public interface KeycloakPort {

    /**
     * Creates a Keycloak group with the given tenant ID as the group name.
     * Returns the Keycloak group ID assigned to the new group.
     */
    void createTenantGroup(TenantId tenantId);

    /**
     * Retrieves all users belonging to the tenant's Keycloak group.
     */
    List<User> getUsersByTenantId(TenantId tenantId);

    /**
     * Assigns a user to the tenant's Keycloak group.
     */
    void assignUserToGroup(String userId, String groupId);

    /**
     * Removes a user from the tenant's Keycloak group.
     */
    void removeUserFromGroup(String userId, String groupId);

    /**
     * Finds the Keycloak group ID associated with the given tenant.
     */
    Optional<String> findGroupIdByTenantId(TenantId tenantId);

    /**
     * Deletes the Keycloak group associated with the given tenant.
     */
    void deleteTenantGroup(TenantId tenantId);
}
