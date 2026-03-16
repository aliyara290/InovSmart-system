package com.aliyara.companyservice.adapters.out.keycloak.dto;

import java.util.List;

public class KeycloakGroupRepresentation {
    private String id;
    private String name;
    private String path;
    private List<KeycloakGroupRepresentation> subGroups;

    public KeycloakGroupRepresentation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<KeycloakGroupRepresentation> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<KeycloakGroupRepresentation> subGroups) {
        this.subGroups = subGroups;
    }


    @Override
    public String toString() {
        return "KeycloakGroupRepresentation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", subGroups=" + subGroups +
                '}';
    }
}
