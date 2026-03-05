package com.aliyara.companyservice.infrastrecture.adapter.keycloak.client;

import com.aliyara.companyservice.config.FeignConfig;
import com.aliyara.companyservice.infrastrecture.adapter.keycloak.dto.KeycloakGroupRequest;
import com.aliyara.companyservice.infrastrecture.adapter.keycloak.dto.KeycloakGroupResponse;
import com.aliyara.companyservice.infrastrecture.adapter.keycloak.dto.KeycloakUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "keycloak", url = "http://localhost:8090", configuration = FeignConfig.class)
public interface KeycloakFeignClient {

    @PostMapping("/admin/realms/${keycloak.realm}/groups")
    void createGroup(@RequestBody KeycloakGroupRequest groupRequest);

    @GetMapping("/admin/realms/${keycloak.realm}/groups")
    List<KeycloakGroupResponse> searchGroups(@RequestParam("search") String search);

    @GetMapping("/admin/realms/${keycloak.realm}/groups/{groupId}/members")
    List<KeycloakUserResponse> getGroupUsers(@PathVariable("groupId") String groupId);

}
