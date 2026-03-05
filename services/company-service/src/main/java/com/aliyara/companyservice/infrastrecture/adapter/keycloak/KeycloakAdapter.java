package com.aliyara.companyservice.infrastrecture.adapter.keycloak;

import com.aliyara.companyservice.domain.model.User;
import com.aliyara.companyservice.domain.model.ids.TenantId;
import com.aliyara.companyservice.domain.port.KeycloakPort;
import com.aliyara.companyservice.infrastrecture.adapter.keycloak.client.KeycloakFeignClient;
import com.aliyara.companyservice.infrastrecture.adapter.keycloak.dto.KeycloakGroupRequest;
import com.aliyara.companyservice.infrastrecture.adapter.keycloak.dto.KeycloakGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KeycloakAdapter implements KeycloakPort {

    private static final String TENANT_PREFIX = "tenant-";

    private final KeycloakFeignClient keycloakFeignClient;

    @Override
    public void createTenantGroup(TenantId tenantId) {
        String groupName = TENANT_PREFIX + tenantId.id().toString();
        KeycloakGroupRequest request = new KeycloakGroupRequest(groupName);
        keycloakFeignClient.createGroup(request);
    }

    @Override
    public List<User> getUsersByTenantId(TenantId tenantId) {
        String groupName = TENANT_PREFIX + tenantId.id().toString();

        List<KeycloakGroupResponse> groups = keycloakFeignClient.searchGroups(groupName);

        return groups.stream()
                .filter(g -> g.getName().equals(groupName))
                .findFirst()
                .map(g -> keycloakFeignClient.getGroupUsers(g.getId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(u -> User.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .email(u.getEmail())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .enabled(u.isEnabled())
                        .build())
                .collect(Collectors.toList());
    }
}
