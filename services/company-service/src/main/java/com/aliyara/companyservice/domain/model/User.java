package com.aliyara.companyservice.domain.model;

import lombok.Getter;

import java.util.List;

@Getter
public class User {

    private final String id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final boolean enabled;
    private final List<String> roles;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.enabled = builder.enabled;
        this.roles = builder.roles != null ? List.copyOf(builder.roles) : List.of();
    }

    public static class Builder {
        private String id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private boolean enabled = true;
        private List<String> roles;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
}
