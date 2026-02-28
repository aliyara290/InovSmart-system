package com.aliyara.companyservice.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

public class Company {

    private final String id;
    private final String tenantId;
    private String name;
    private String email;
    private String phone;
    private String website;
    private int size;
    private int foundedYear;
    private String description;
    private Address address;
    private CompanySettings companySettings;

    private Company(Builder builder) {
        validate(builder);
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.website = builder.website;
        this.size = builder.size;
        this.foundedYear = builder.foundedYear;
        this.description = builder.description;
        this.address = builder.address;
        this.companySettings = builder.companySettings;
    }

    public static class Builder {
        private String id;
        private String tenantId;
        private String name;
        private String email;
        private String phone;
        private String website;
        private int size;
        private int foundedYear;
        private String description;
        private Address address;
        private CompanySettings companySettings;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder foundedYear(int foundedYear) {
            this.foundedYear = foundedYear;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder companySettings(CompanySettings companySettings) {
            this.companySettings = companySettings;
            return this;
        }

        public Company build() {
            return new Company(this);
        }
    }

    private void validate(Builder builder) {
        if (builder.name == null || builder.name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }

        if (builder.email == null || !builder.email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
}