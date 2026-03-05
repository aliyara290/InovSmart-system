package com.aliyara.companyservice.infrastrecture.adapter.keycloak.dto;

import lombok.Data;

@Data
public class KeycloakGroupResponse {
    private String id;
    private String name;
    private String path;
}
