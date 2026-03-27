package com.aliyara.companyservice.adapters.out.keycloak.client;

import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakUserRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "keycloak-admin-api",
    contextId = "keycloak-user-client",
    url = "${keycloak.base-url}",
    configuration = com.aliyara.companyservice.adapters.out.keycloak.config.KeycloakAdminFeignConfig.class
)
public interface KeycloakUserClient {

    @PostMapping("/admin/realms/${keycloak.realm}/users")
    ResponseEntity<Void> createUser(@RequestBody KeycloakUserRepresentation user);

    @GetMapping("/admin/realms/${keycloak.realm}/users/{userId}")
    KeycloakUserRepresentation getUser(@PathVariable("userId") String userId);

    @DeleteMapping("/admin/realms/${keycloak.realm}/users/{userId}")
    void deleteUser(@PathVariable("userId") String userId);

    @PutMapping("/admin/realms/${keycloak.realm}/users/{userId}/groups/{groupId}")
    void addUserToGroup(@PathVariable("userId") String userId, @PathVariable("groupId") String groupId);

    @DeleteMapping("/admin/realms/${keycloak.realm}/users/{userId}/groups/{groupId}")
    void removeUserFromGroup(@PathVariable("userId") String userId, @PathVariable("groupId") String groupId);

    @PutMapping("/admin/realms/${keycloak.realm}/users/{userId}/execute-actions-email")
    void executeActionsEmail(
            @PathVariable("userId") String userId, 
            @RequestParam(value = "client_id", required = false) String clientId,
            @RequestParam(value = "lifespan", required = false) Integer lifespan,
            @RequestBody List<String> actions);
}

