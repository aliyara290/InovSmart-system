package com.aliyara.companyservice.adapters.out.keycloak.dto;

import java.util.List;
import java.util.Map;

public class KeycloakUserRepresentation {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private Boolean emailVerified;
    private Map<String, List<String>> attributes;
    private List<KeycloakCredentialRepresentation> credentials;

    public KeycloakUserRepresentation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public List<KeycloakCredentialRepresentation> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<KeycloakCredentialRepresentation> credentials) {
        this.credentials = credentials;
    }

    public static class KeycloakCredentialRepresentation {
        private String type;
        private String value;
        private Boolean temporary;

        public KeycloakCredentialRepresentation() {
        }

        public KeycloakCredentialRepresentation(String type, String value, Boolean temporary) {
            this.type = type;
            this.value = value;
            this.temporary = temporary;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Boolean getTemporary() {
            return temporary;
        }

        public void setTemporary(Boolean temporary) {
            this.temporary = temporary;
        }

        @Override
        public String toString() {
            return "KeycloakCredentialRepresentation{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    ", temporary=" + temporary +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "KeycloakUserRepresentation{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", enabled=" + enabled +
                ", emailVerified=" + emailVerified +
                ", attributes=" + attributes +
                ", credentials=" + credentials +
                '}';
    }
}
