package com.aliyara.companyservice.adapters.out.keycloak.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KeycloakRoleRepresentation {
    private String id;
    private String name;
}
