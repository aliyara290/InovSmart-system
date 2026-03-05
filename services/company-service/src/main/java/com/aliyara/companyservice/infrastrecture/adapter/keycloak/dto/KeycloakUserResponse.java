package com.aliyara.companyservice.infrastrecture.adapter.keycloak.dto;

import lombok.Data;

@Data
public class KeycloakUserResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
}
