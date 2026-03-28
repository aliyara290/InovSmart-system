package com.aliyara.companyservice.adapters.out.keycloak.client;

import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakGroupRepresentation;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakRoleRepresentation;
import com.aliyara.companyservice.adapters.out.keycloak.dto.KeycloakUserRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "keycloak-admin-api",
    contextId = "keycloak-group-client",
    url = "${keycloak.base-url}",
    configuration = com.aliyara.companyservice.adapters.out.keycloak.config.KeycloakAdminFeignConfig.class
)
public interface KeycloakGroupClient {

    @PostMapping("/admin/realms/${keycloak.realm}/groups")
    ResponseEntity<Void> createGroup(@RequestBody KeycloakGroupRepresentation group);

    @GetMapping("/admin/realms/${keycloak.realm}/groups")
    List<KeycloakGroupRepresentation> searchGroups(@RequestParam("search") String search);

    @PostMapping("/admin/realms/${keycloak.realm}/groups/{groupId}/children")
    ResponseEntity<Void> createSubgroup(@PathVariable("groupId") String groupId, 
                                        @RequestBody KeycloakGroupRepresentation subgroup);

    @GetMapping("/admin/realms/${keycloak.realm}/groups/{groupId}")
    KeycloakGroupRepresentation getGroup(@PathVariable("groupId") String groupId, 
                                         @RequestParam(value = "briefRepresentation", defaultValue = "false") boolean briefRepresentation);

    @GetMapping("/admin/realms/${keycloak.realm}/groups/{groupId}/children")
    List<KeycloakGroupRepresentation> getGroupChildren(@PathVariable("groupId") String groupId);

    @GetMapping("/admin/realms/${keycloak.realm}/groups/{groupId}/members")
    List<KeycloakUserRepresentation> getGroupMembers(@PathVariable("groupId") String groupId);


    @GetMapping("/admin/realms/${keycloak.realm}/roles/{name}")
    KeycloakRoleRepresentation getRealmRole(@PathVariable String name);

    @PostMapping("/admin/realms/${keycloak.realm}/groups/{groupId}/role-mappings/realm")
    Void attachRoleToGroup(@PathVariable String groupId, @RequestBody List<KeycloakRoleRepresentation> roles);

}
